package org.jetbrains.plugins.scala.lang.parser.parsing.types

import org.jetbrains.plugins.scala.lang.parser.ScalaElementTypes
import org.jetbrains.plugins.scala.lang.parser.parsing.builder.ScalaPsiBuilder
import org.jetbrains.plugins.scala.lang.parser.parsing.expressions._

/** 
* @author Alexander Podkhalyuzin
* Date: 06.02.2008
*/

/*
 * AnnotType ::= {Annotation} SimpleType
 */
object AnnotType extends AnnotType {
  override protected def annotation = Annotation
  override protected def simpleType = SimpleType
}

trait AnnotType {
  protected def annotation: Annotation
  protected def simpleType: SimpleType

  def parse(builder: ScalaPsiBuilder, isPattern: Boolean, multipleSQBrackets: Boolean = true): Boolean = {
    val annotMarker = builder.mark
    var isAnnotation = false
    //parse Simple type
    if (simpleType.parse(builder, isPattern, multipleSQBrackets)) {
      val annotationsMarker = builder.mark
      while (!builder.newlineBeforeCurrentToken && annotation.parse(builder,
        countLinesAfterAnnotation = false)) {isAnnotation = true}

      if (isAnnotation) annotationsMarker.done(ScalaElementTypes.ANNOTATIONS) else annotationsMarker.drop()
      if (isAnnotation) annotMarker.done(ScalaElementTypes.ANNOT_TYPE) else annotMarker.drop()
      true
    } else {
      annotMarker.rollbackTo()
      false
    }
  }
}