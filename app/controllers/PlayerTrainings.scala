package controllers

import java.util.Date
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.mvc._
import models.{Training,Player,PlayerTraining}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.text.SimpleDateFormat

object PlayerTrainings extends Controller with Secured {
    val playerTrainingForm = Form(tuple(
    "player" -> nonEmptyText,
    "training" -> nonEmptyText,
    "date" -> date
    )
  )
  

  def index = isAdmin { implicit request =>
    val filledForm = playerTrainingForm.fill(("","Basic Infantry",new Date))
    Ok(views.html.playertrainings.index(filledForm,Training.all,Player.all))
  }

  implicit val rds = (
    (__ \ 'player).read[String] and
    (__ \ 'training).read[String] and
    (__ \ 'date).read[String]
  ).tupled
  
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    
  def train = isAdmin(parse.json) { implicit request =>
    request.body.validate[(String,String,String)].map {
      case (playerName,trainingName,date) => {
        val parsedDate = dateFormat.parse(date)
        val res = Player.find(playerName).map { p => 
          Training.find(trainingName).map { t=>
            val pt = PlayerTraining(p,t,parsedDate)
            PlayerTraining.add(pt)
            
            Ok("OK")
          }
        }
        
        res.flatten match {
          case Some(x) => x
          case None => BadRequest("Unable to extract data") 
        }
      }
    }.recoverTotal {
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
    }
  }
//  def sayHello = Action(parse.json) { request =>
//    request.body.validate[(String, Long)].map{ 
//      case (name, age) => Ok("Hello " + name + ", you're "+age)
//    }.recoverTotal{
//      e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
//    }
//  }
}