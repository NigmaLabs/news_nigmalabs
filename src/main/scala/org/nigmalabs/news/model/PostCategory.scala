package org.nigmalabs.news {
package model {

import net.liftweb.mapper._


object PostCategory extends PostCategory with LongKeyedMetaMapper[PostCategory] {
  override def dbTableName = "post_category"
}

class PostCategory extends LongKeyedMapper[PostCategory] with IdPK {

  def getSingleton = PostCategory
  
  object postid extends LongMappedMapper(this, Post)
  object categoryid extends LongMappedMapper(this, Category)

}


}
}
