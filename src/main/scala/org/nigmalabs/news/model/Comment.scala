package org.nigmalabs.news {
package model {

import net.liftweb.mapper._
import net.liftweb.common.{Full}

object Comment extends Comment with LongKeyedMetaMapper[Comment] {
  override def dbTableName = "comments"
}

class Comment extends LongKeyedMapper[Comment] with IdPK with CreatedUpdated {

  def getSingleton = Comment
  
  object postid extends LongMappedMapper(this, Post) {
    override def validSelectValues = 
      Full(Post.findMap(OrderBy(Post.id, Ascending)) {
        case r: Post => Full(r.id.is -> r.title.is)
      })
  }
  object threadid extends MappedLongForeignKey(this, Comment) {
    def defualtValue = 0L
  }
  object author extends MappedString(this, 128)
  object userid extends LongMappedMapper(this, User) {
    override def validSelectValues = 
      Full(User.findMap(OrderBy(User.id, Ascending)) {
        case r: User => Full(r.id.is -> r.email.is)
      })
    override def defaultValue = User.currentUser match {
      case Full(user) => user.id.is
      case _ => 0L
    }
	}
	object comment extends MappedText(this)

}

}
}
