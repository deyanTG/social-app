package uni.social.connect.conf;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.*;
import ma.glasnost.orika.impl.generator.specification.*;
import ma.glasnost.orika.metadata.FieldMap;
import ma.glasnost.orika.metadata.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OrikaConfig {

	private static class Object2EnumConverter extends BidirectionalConverter<Object, Enum> {
		@Override
		public Enum convertTo(Object source, Type<Enum> destinationType) {
			if (source == null || StringUtils.isEmpty(source)) {
				return null;
			}
			return Enum.valueOf(destinationType.getRawType(), String.valueOf(source));
		}

		@Override
		public Object convertFrom(Enum source, Type<Object> destinationType) {
			if (source == null) {
				return null;
			}
			return source.name();
		}

		@Override
		public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
			return sourceType.getRawType().isEnum() || destinationType.getRawType().isEnum();
		}
	}

	// FIXME: Does not work :(
	@SuppressWarnings("unused")
	private static class GenericMapConverter<T> extends BidirectionalConverter<Map<String, Object>, T> {
		private final Class<T> type;

		public GenericMapConverter(Class<T> type) {
			this.type = type;
		}

		@Override
		public T convertTo(Map<String, Object> source, Type<T> destinationType) {
			if (source == null) {
				return null;
			}
			return mapperFacade.map(source, type);
		}

		@Override
		public Map<String, Object> convertFrom(T source, Type<Map<String, Object>> destinationType) {
			if (source == null) {
				return null;
			}
			return mapperFacade.map(source, HashMap.class);
		}

		@Override
		public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
			return (sourceType.isAssignableFrom(type) && destinationType.isMap())
					|| (destinationType.isAssignableFrom(type) && sourceType.isMap());
		}
	}

	public static class LocalDateTimeLongConverter extends BidirectionalConverter<LocalDateTime, Long> {
		@Override
		public Long convertTo(LocalDateTime source, Type<Long> destinationType) {
			if (source == null) {
				return null;
			}
			return source.toEpochSecond(ZoneOffset.UTC);
		}

		@Override
		public LocalDateTime convertFrom(Long source, Type<LocalDateTime> destinationType) {
			if (source == null) {
				return null;
			}
			return LocalDateTime.ofEpochSecond(source, 0, ZoneOffset.UTC);
		}
	}

	public static class LocalDateLongConverter extends BidirectionalConverter<LocalDate, Long> {

		@Override
		public Long convertTo(LocalDate source, Type<Long> destinationType) {
			if (source == null) {
				return null;
			}
			return source.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
		}

		@Override
		public LocalDate convertFrom(Long source, Type<LocalDate> destinationType) {
			if (source == null) {
				return null;
			}
			return LocalDateTime.ofEpochSecond(source, 0, ZoneOffset.UTC).toLocalDate();
		}
	}

	public static class LocalDateStringConverter extends BidirectionalConverter<LocalDate, String> {

		@Override
		public String convertTo(LocalDate source, Type<String> destinationType) {
			if (source == null) {
				return null;
			}
			return source.toString();
		}

		@Override
		public LocalDate convertFrom(String source, Type<LocalDate> destinationType) {
			if (source == null) {
				return null;
			}
			return LocalDate.parse(source, DateTimeFormatter.ISO_DATE);
		}
	}

	public static class LocalDateTimeStringConverter extends BidirectionalConverter<LocalDateTime, String> {

		@Override
		public String convertTo(LocalDateTime source, Type<String> destinationType) {
			if (source == null) {
				return null;
			}
			return source.toString();
		}

		@Override
		public LocalDateTime convertFrom(String source, Type<LocalDateTime> destinationType) {
			if (source == null) {
				return null;
			}
			return LocalDateTime.parse(source, DateTimeFormatter.ISO_DATE_TIME);
		}
	}

	@Bean
	public static MapperFacade orikaMapperFacade() {
		IfInitializedHibernateMapperFactory mapperFactory = new IfInitializedHibernateMapperFactory.Builder()
				.useAutoMapping(true).mapNulls(true).build();

		mapperFactory.getConverterFactory().registerConverter(new LocalDateTimeLongConverter());
		mapperFactory.getConverterFactory().registerConverter(new LocalDateTimeStringConverter());
		mapperFactory.getConverterFactory().registerConverter(new LocalDateLongConverter());
		mapperFactory.getConverterFactory().registerConverter(new LocalDateStringConverter());
		mapperFactory.getConverterFactory().registerConverter(new Object2EnumConverter());

		return mapperFactory.getMapperFacade();
	}

	public static class IfInitializedHibernateCGS implements CodeGenerationStrategy {

		private List<Specification> specs = new ArrayList<Specification>();

		private final ArrayList<AggregateSpecification> aggregateSpecifications;

		public IfInitializedHibernateCGS() {
			super();
			specs.add(new IfInitializedHibernateSpecification(new Convert()));
			specs.add(new IfInitializedHibernateSpecification(new CopyByReference()));
			specs.add(new IfInitializedHibernateSpecification(new ApplyRegisteredMapper()));
			specs.add(new IfInitializedHibernateSpecification(new EnumToEnum()));
			specs.add(new IfInitializedHibernateSpecification(new StringToEnum()));
			specs.add(new IfInitializedHibernateSpecification(new UnmappableEnum()));
			specs.add(new IfInitializedHibernateSpecification(new ArrayOrCollectionToArray()));
			specs.add(new IfInitializedHibernateSpecification(new ArrayOrCollectionToCollection()));
			specs.add(new IfInitializedHibernateSpecification(new MapToMap()));
			specs.add(new IfInitializedHibernateSpecification(new MapToArray()));
			specs.add(new IfInitializedHibernateSpecification(new MapToCollection()));
			specs.add(new IfInitializedHibernateSpecification(new ArrayOrCollectionToMap()));
			specs.add(new IfInitializedHibernateSpecification(new StringToStringConvertible()));
			specs.add(new IfInitializedHibernateSpecification(new AnyTypeToString()));
			specs.add(new IfInitializedHibernateSpecification(new MultiOccurrenceElementToObject()));
			specs.add(new IfInitializedHibernateSpecification(new ObjectToMultiOccurrenceElement()));
			specs.add(new IfInitializedHibernateSpecification(new PrimitiveAndObject()));
			specs.add(new IfInitializedHibernateSpecification(new ObjectToObject()));

			this.aggregateSpecifications = new ArrayList<AggregateSpecification>();
			aggregateSpecifications.add(new MultiOccurrenceToMultiOccurrence());
		}

		public void setMapperFactory(MapperFactory mapperFactory) {
			for (Specification spec : this.specs) {
				spec.setMapperFactory(mapperFactory);
			}
			for (AggregateSpecification spec : this.aggregateSpecifications) {
				spec.setMapperFactory(mapperFactory);
			}
		}

		public List<Specification> getSpecifications() {
			return specs;
		}

		public void addAggregateSpecification(AggregateSpecification spec, Position relativePosition,
				Class<AggregateSpecification> relativeSpec) {
			throw new UnsupportedOperationException();
		}

		public List<AggregateSpecification> getAggregateSpecifications() {
			return aggregateSpecifications;
		}

		public void addSpecification(Specification spec, Position relativePosition, Class<Specification> relativeSpec) {
			throw new UnsupportedOperationException();
		}
	}

	public static class IfInitializedHibernateMapperFactory extends DefaultMapperFactory {

		public IfInitializedHibernateMapperFactory(MapperFactoryBuilder<?, ?> builder) {
			super(builder);
		}

		public static abstract class IfInitializedHibernateMapperFactoryBuilder<F extends DefaultMapperFactory, B extends MapperFactoryBuilder<F, B>>
				extends MapperFactoryBuilder<F, B> {

			public IfInitializedHibernateMapperFactoryBuilder() {
				super();
				codeGenerationStrategy = new IfInitializedHibernateCGS();
			}
		}

		public static class Builder
				extends IfInitializedHibernateMapperFactoryBuilder<IfInitializedHibernateMapperFactory, Builder> {

			@Override
			public IfInitializedHibernateMapperFactory build() {
				return new IfInitializedHibernateMapperFactory(this);
			}

			@Override
			protected Builder self() {
				return this;
			}
		}
	}

	public static class IfInitializedHibernateSpecification extends AbstractSpecification {

		private Specification delegate;

		public IfInitializedHibernateSpecification(Specification delegate) {
			this.delegate = delegate;
		}

		@Override
		public boolean appliesTo(FieldMap fieldMap) {
			return delegate.appliesTo(fieldMap);
		}

		@Override
		public String generateMappingCode(FieldMap fieldMap, VariableRef source, VariableRef destination,
				SourceCodeContext code) {
			StringBuilder sb = new StringBuilder();

			sb.append(String.format("if(org.hibernate.Hibernate.isInitialized(%s)) {", source.asWrapper()));
			sb.append(delegate.generateMappingCode(fieldMap, source, destination, code));
			sb.append("}\n");

			return sb.toString();
		}

		@Override
		public void setMapperFactory(MapperFactory mapperFactory) {
			super.setMapperFactory(mapperFactory);
			delegate.setMapperFactory(mapperFactory);
		}
	}
}
