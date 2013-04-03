package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._
import views._

object Application extends Controller {
  val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    ) verifying ("Invalid username or password", result => result match {
      case (username, password) => Users.authenticate(username, password).isDefined
    })
  )

  def index = Action {
    Redirect(routes.Players.form)
  }
  
  /**
   * Login page.
   */
  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => {
        val username = user._1
        if(username=="admin")
          Redirect(routes.Ranks.list).withSession("user" -> username)
        else
          Redirect(routes.Players.form).withSession("user" -> username)
      }
    )
  }

  /**
   * Logout and clean the session.
   */
  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  def javascriptRoutes = Action { implicit request =>
    import routes.javascript._
    Ok(
      Routes.javascriptRouter("jsRoutes")(routes.javascript.Players.delete)).as("text/javascript")
  }
}

/**
 * Provide security features
 */
trait Secured {
  
  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get("user")
  
  private def adminUsername(request: RequestHeader) = if(username(request) == Some("admin")) Some("admin") else None

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)
  
  /** 
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
    Action(request => f(request))
  }
  
  def isAdmin(f: => Request[AnyContent] => Result) = Security.Authenticated(adminUsername, onUnauthorized) { user =>
    Action(request => f(request))
  }
}

