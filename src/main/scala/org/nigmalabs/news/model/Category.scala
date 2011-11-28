package org.nigmalabs.news {
package model {

import net.liftweb.mapper._

object Category extends Category with LongKeyedMetaMapper[Category] {
  override def dbTableName = "categories"
}

class Category extends LongKeyedMapper[Category] with IdPK with ManyToMany {

  def getSingleton = Category

}

}
}
