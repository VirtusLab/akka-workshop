package controllers

import com.virtuslab.akkaworkshop.PasswordsDistributor._
import play.api.libs.json.Json

object JsonModelFormats {

  implicit val registerFormat                = Json.format[Register]
  implicit val registeredFormat              = Json.format[Registered]
  implicit val sendMeEncryptedPasswordFormat = Json.format[SendMeEncryptedPassword]
  implicit val encryptedPasswordFormat       = Json.format[EncryptedPassword]
  implicit val validateDecodedPassword       = Json.format[ValidateDecodedPassword]
  implicit val passwordCorrect               = Json.format[PasswordCorrect]
  implicit val passwordIncorrect             = Json.format[PasswordIncorrect]

}
