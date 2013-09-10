package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

object Images extends Controller {
  def get(name:String,file:String) = controllers.Assets.at("/public/images", file)
}