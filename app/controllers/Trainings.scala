package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import models.Training

object Trainings  extends Controller with Secured {
  val trainingForm = Form("name" -> nonEmptyText)
  
  def list = isAdmin { implicit request =>
    Ok(views.html.trainings.list(Training.all,trainingForm))
  }
  
  def create = isAdmin { implicit request =>
    
    trainingForm.bindFromRequest.fold(
      errors => BadRequest(views.html.trainings.list(Training.all,errors)),
      name => {
      	Training.create(name)
        Ok(views.html.trainings.list(Training.all,trainingForm)).flashing("message"->"Training created")
      }
    )
  }
  
  def delete(name:String) = isAdmin { implicit request => 
    Training.delete(name)
    Ok("done")
  }
}