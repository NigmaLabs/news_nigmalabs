package org.nigmalabs.news {
package model {

import net.liftweb.mapper._
import net.liftweb.common.{Full}

object Post extends Post with LongKeyedMetaMapper[Post] {
  override def dbTableName = "posts"
}

class Post extends LongKeyedMapper[Post] with IdPK with CreatedUpdated {

  def getSingleton = Post

  object title extends MappedString(this, 128)
  object content extends MappedText(this)
  object url extends MappedString(this, 512)
  object author extends LongMappedMapper(this, User) {
    override def validSelectValues = 
      Full(User.findMap(OrderBy(User.id, Ascending)) {
        case r: User => Full(r.id.is -> r.email.is)
      })
	}
	object category extends 

}

}
}
