package uni.social.connect.conf;

import org.springframework.context.annotation.Conditional;

@Conditional(value = PropertyCondition.class)
public @interface PropertyConditional {

	String value();
	
	String propertyValue() default "";
	
}
