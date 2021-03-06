package bootstrap.liftweb

import net.liftweb._
import http.{LiftRules, NotFoundAsTemplate, ParsePath}
import sitemap.{SiteMap, Menu, Loc}
import util.{ NamedPF }
import _root_.net.liftweb.sitemap.Loc._
import net.liftweb._
import mapper.{Schemifier, DB, StandardDBVendor, DefaultConnectionIdentifier}
import util.{Props}
import common.{Full}
import http.{S}
import org.nigmalabs.news.model._


class Boot {
  def boot {
  
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = new StandardDBVendor(Props.get("db.driver") openOr "org.postgresql.Driver",
			         Props.get("db.url") openOr "jdbc:postgresql:news",
			         Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, User, Post, Category, PostCategory, Comment)
  
    // where to search snippet
    LiftRules.addToPackages("org.nigmalabs.news")

    // build sitemap
    val entries = 
      List(
        Menu.i("Home") / "index",
        Menu.i("Create post") / "post" / "create" >> User.loginFirst
        ) ::: User.sitemap ::: Nil
    
    LiftRules.uriNotFound.prepend(NamedPF("404handler"){
      case (req,failure) => NotFoundAsTemplate(
        ParsePath(List("exceptions","404"),"html",false,false))
    })
    
    LiftRules.setSiteMap(SiteMap(entries:_*))
    
    // set character encoding
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))
    
    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)
    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)
    
  }
}
