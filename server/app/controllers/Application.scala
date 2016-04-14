package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout

import com.virtuslab.akkaworkshop._

object Application extends Controller {

  implicit val timeout = Timeout(5.seconds)

  def index = Action {req => Ok(views.html.index(req.getQueryString("mode").getOrElse("all")))}


  def orderClients(stats: Seq[Client], mode: Option[String]): Seq[Client] = {
    mode match {
      case Some("remote") =>
        stats.sortBy(- _.timestamp)
      case Some("parallel") =>
        stats.sortBy(- _.passwordsPerMinute)
      case other =>
        stats.sortBy(- _.precentOfCorrect)
    }

  }


  def leaderboard = Action.async { req =>
    import PasswordsDistributor._
    val distributor = Akka.system.actorSelection("akka.tcp://application@headquarters:9552/user/PasswordsDistributor")

    distributor ? SendMeStatistics map {
      case statistics: Statistics =>
        val clients = orderClients(statistics.clients, req.getQueryString("order"))
        Ok(views.html.leaderboard(clients))
    }
  }
}