import akka.actor.{ActorRef, ActorSystem}
import com.virtuslab.akkaworkshop._
import javax.inject.{Inject, Singleton}

@Singleton
class StartDistributor @Inject()(actorSystem: ActorSystem) {

  val distributor = actorSystem.actorOf(PasswordsDistributor.props, name = "PasswordsDistributor")
  testDistributor(actorSystem, distributor)

  private def testDistributor(actorSystem: ActorSystem, distributor: ActorRef) {
    import PasswordsDistributor._
    import akka.pattern.ask
    import akka.util.Timeout
    import org.apache.commons.codec.binary.Base64

    import scala.concurrent.Await
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._
    import scala.util.Random

    implicit val timeout = Timeout(5.seconds)

    def updateClient(token: String): Unit = {
      val encryptedPassword = Await
        .result(distributor ? SendMeEncryptedPassword(token), timeout.duration)
        .asInstanceOf[EncryptedPassword]
        .encryptedPassword

      def decrypt(p: String): String = new String(Base64.decodeBase64(p.getBytes))
      val decryptedPassword          = decrypt(decrypt(decrypt(encryptedPassword)))

      if (Random.nextBoolean())
        distributor ! ValidateDecodedPassword(token, encryptedPassword, decryptedPassword)
      else
        distributor ! ValidateDecodedPassword(token, encryptedPassword, encryptedPassword)
    }

    List("John Doe", "Jan Kowalski") map { case name =>
      val token = Await
        .result(distributor ? Register(name, "Java"), timeout.duration)
        .asInstanceOf[Registered]
        .token

      actorSystem.scheduler
        .schedule(0.seconds, (2 + Random.nextInt(5)).seconds)(updateClient(token))
    }
  }
}
