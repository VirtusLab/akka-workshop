package controllers

import akka.actor.ActorSystem

import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import com.virtuslab.akkaworkshop.PasswordsDistributor._
import com.virtuslab.akkaworkshop._
import javax.inject._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton()
class HomeController @Inject()(actorSystem: ActorSystem, cc: ControllerComponents)(
  implicit assetsFinder: AssetsFinder,
  ec: ExecutionContext
) extends AbstractController(cc) {

  import JsonModelFormats._

  implicit val timeout = Timeout(5.seconds)
  val distributor      = actorSystem.actorSelection("akka.tcp://application@headquarters:9552/user/PasswordsDistributor")

  def index: Action[AnyContent] = Action { req =>
    Ok(views.html.index(req.getQueryString("mode").getOrElse("all")))
  }

  def register: Action[JsValue] = Action.async(parse.json) { req =>
    val register = req.body.as[Register]
    (distributor ? register).mapTo[Registered].map { registered =>
      Ok(Json.toJson(registered))
    }
  }

  def sendEncryptedPassword: Action[JsValue] = Action.async(parse.json) { req =>
    val sendMeEncryptedPassword = req.body.as[SendMeEncryptedPassword]
    (distributor ? sendMeEncryptedPassword).mapTo[EncryptedPassword].map { encryptedPassword =>
      Ok(Json.toJson(encryptedPassword))
    }
  }

  def validate: Action[JsValue] = Action.async(parse.json) { req =>
    val validate = req.body.as[ValidateDecodedPassword]
    (distributor ? validate).mapTo[PDMessageResponse].map {
      case correct: PasswordCorrect     => Ok(Json.toJson(correct))
      case incorrect: PasswordIncorrect => BadRequest(Json.toJson(incorrect))
      case _                            => BadRequest(Json.toJson(Json.toJson("Unexpected message")))
    }
  }

  def leaderboard: Action[AnyContent] = Action.async { req =>
    import PasswordsDistributor._

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
