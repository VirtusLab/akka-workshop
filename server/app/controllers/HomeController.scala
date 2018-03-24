package controllers

import akka.actor.ActorSystem

import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import com.virtuslab.akkaworkshop._
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton()
class HomeController @Inject()(actorSystem: ActorSystem, cc: ControllerComponents)(
  implicit assetsFinder: AssetsFinder,
  ec: ExecutionContext
) extends AbstractController(cc) {

  implicit val timeout = Timeout(5.seconds)

  def index: Action[AnyContent] = Action { req =>
    Ok(views.html.index(req.getQueryString("mode").getOrElse("all")))
  }

  def leaderboard: Action[AnyContent] = Action.async { req =>
    import PasswordsDistributor._
    val distributor = actorSystem.actorSelection("akka.tcp://application@headquarters:9552/user/PasswordsDistributor")

    distributor ? SendMeStatistics map {
      case statistics: Statistics =>
        val clients =
          orderClients(statistics.clients, req.getQueryString("order"))
        Ok(views.html.leaderboard(clients))
    }
  }

  private def orderClients(stats: Seq[Client], mode: Option[String]): Seq[Client] = {
    mode match {
      case Some("remote") =>
        stats.sortBy(-_.timestamp)
      case Some("parallel") =>
        stats.sortBy(-_.passwordsPerMinute)
      case _ =>
        stats.sortBy(-_.precentOfCorrect)
    }

  }
}
