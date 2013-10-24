package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import models.Player

object Archived extends Controller with Secured {
  val playerNameForm = Form(
    "name" -> nonEmptyText
    )
  
  def list = isAdmin { implicit request => 
    val archivedPlayers = Player.allArchived
    Ok(views.html.archived.list(archivedPlayers,playerNameForm))
  }
  
  def unarchive = isAdmin { implicit request =>
    playerNameForm.bindFromRequest.fold(
      errors => Redirect(routes.Archived.list),
      name => {
        Player.restore(name)
        Redirect(routes.Archived.list)
      }
    )
  }
}