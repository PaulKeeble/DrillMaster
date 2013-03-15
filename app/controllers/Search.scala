package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import models.Player

object Search extends Controller with Secured {
  val searchForm = Form(
    "q" -> text
  )
  
  def form = IsAuthenticated { implicit request =>
    Ok(views.html.search.findPlayer(searchForm))
  }
  
  def find() = IsAuthenticated { implicit request =>
    searchForm.bindFromRequest.fold(
      errors => BadRequest(views.html.search.findPlayer(errors)),
      q => {
        Player.find(q) match {
          case Some(player) => Redirect(routes.Players.show(player.name))
          case None => Redirect(routes.Search.form).flashing("message"->"player not found")
        }
      } 
    )
  }
}