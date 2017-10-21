package com.anosi.asset.model.elasticsearch;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 这个注解用来标记bean中哪些属性需要被提取，然后对这些内容进行全文索引
 * 
 * @author jinyao
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface Content {

	String[] extractFields() default {};
	
}
