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
    "training" -> nonEmptyText,
    "date" -> date,
    "trainer" -> nonEmptyText
    )
  )

  def index = isAdmin { implicit request =>
    val filledForm = playerTrainingForm.fill(("Basic Infantry",new Date,""))
    Ok(views.html.playertrainings.index(filledForm,Training.all,Player.all))
  }

  implicit val rds = (
    (__ \ 'player).read[String] and
    (__ \ 'training).read[String] and
    (__ \ 'date).read[String] and
    (__ \ 'trainer).read[String]
  ).tupled
  
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd")

  def train = isAdmin(parse.json) { implicit request =>
    request.body.validate[(String,String,String,String)].map {
      case (playerName,trainingName,date,trainer) => {
        val parsedDate = dateFormat.parse(date)
        val res = Player.find(playerName).map { p => 
          Training.find(trainingName).map { t=>
            val pt = PlayerTraining(p,t,parsedDate,trainer)
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

  def announcements = IsAuthenticated { implicit request =>
    val trainings = PlayerTraining.announcements
    Ok(views.html.playertrainings.announcements(trainings,dateFormat))
  }

  def roster = Action { implicit request =>
    val playersByRank = Player.allPlayersSortedByRank
  	val trainingsByPlayer = PlayerTraining.allTrainingsByPlayer
  	Ok(views.html.playertrainings.roster(playersByRank,trainingsByPlayer))
  }
}