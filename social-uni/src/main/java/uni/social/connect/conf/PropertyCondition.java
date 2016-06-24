package uni.social.connect.conf;

import java.util.Map;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

public class PropertyCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Map<String, Object> attributes = metadata.getAnnotationAttributes(
				"com.connectik.cloverlead.conf.PropertyConditional");
		String property = (String) attributes.get("value");
		String value = (String) attributes.get("propertyValue");

		String actualValue = context.getEnvironment().getProperty(property);

		return StringUtils.isEmpty(value) ? actualValue != null : value.equals(actualValue);
	}

}
