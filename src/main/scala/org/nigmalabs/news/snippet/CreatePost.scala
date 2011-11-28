package org.nigmalabs.news {
package snippet {

import scala.xml.{Elem,NodeSeq}
import net.liftweb.http._
import net.liftweb.common.{Box}

import model._

object CreatePost extends LiftScreen {
   
  val title = field(S.?("Tytuł"), "")
  val content = textarea(S.?("Treść"), "")
  val url = field(S.?("Źródło"), "")
  
  //val categories = 
  
  def finish(){
    val post = Post.create
    post.title(title).content(content).url(url)
    post.validate
  }  
  
}

}
}
