
package org.mule.modules.taleo.model.transformers;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.datatype.XMLGregorianCalendar;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.endpoint.ImmutableEndpoint;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.transformer.DataType;
import org.mule.api.transformer.DiscoverableTransformer;
import org.mule.api.transformer.MessageTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transformer.TransformerMessagingException;
import org.mule.config.i18n.CoreMessages;
import org.mule.devkit.processor.ExpressionEvaluatorSupport;
import org.mule.modules.taleo.model.ArrayOfFlexFieldBean;
import org.mule.modules.taleo.model.ArrayOfXsdLong;
import org.mule.modules.taleo.model.ArrayOfXsdString;
import org.mule.modules.taleo.model.LocationBean;
import org.mule.modules.taleo.model.Map;
import org.mule.modules.taleo.model.holders.AddressEntityBeanExpressionHolder;
import org.mule.modules.taleo.model.holders.EntityBeanExpressionHolder;
import org.mule.modules.taleo.model.holders.LocationBeanExpressionHolder;
import org.mule.transformer.types.DataTypeFactory;

@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.6.1", date = "2015-12-18T03:06:03-03:00", comments = "Build UNNAMED.2405.44720b7")
public class LocationBeanExpressionHolderTransformer
    extends ExpressionEvaluatorSupport
    implements DiscoverableTransformer, MessageTransformer
{

    private int weighting = DiscoverableTransformer.DEFAULT_PRIORITY_WEIGHTING;
    private ImmutableEndpoint endpoint;
    private MuleContext muleContext;
    private String name;

    public int getPriorityWeighting() {
        return weighting;
    }

    public void setPriorityWeighting(int weighting) {
        this.weighting = weighting;
    }

    public boolean isSourceTypeSupported(Class<?> aClass) {
        return (aClass == LocationBeanExpressionHolder.class);
    }

    public boolean isSourceDataTypeSupported(DataType<?> dataType) {
        return (dataType.getType() == LocationBeanExpressionHolder.class);
    }

    public List<Class<?>> getSourceTypes() {
        return Arrays.asList(new Class<?> [] {LocationBeanExpressionHolder.class });
    }

    public List<DataType<?>> getSourceDataTypes() {
        return Arrays.asList(new DataType<?> [] {DataTypeFactory.create(LocationBeanExpressionHolder.class)});
    }

    public boolean isAcceptNull() {
        return false;
    }

    public boolean isIgnoreBadInput() {
        return false;
    }

    public Object transform(Object src)
        throws TransformerException
    {
        throw new UnsupportedOperationException();
    }

    public Object transform(Object src, String encoding)
        throws TransformerException
    {
        throw new UnsupportedOperationException();
    }

    public void setReturnClass(Class<?> theClass) {
        throw new UnsupportedOperationException();
    }

    public Class<?> getReturnClass() {
        return LocationBean.class;
    }

    public void setReturnDataType(DataType<?> type) {
        throw new UnsupportedOperationException();
    }

    public DataType<?> getReturnDataType() {
        return DataTypeFactory.create(LocationBean.class);
    }

    public void setEndpoint(ImmutableEndpoint ep) {
        endpoint = ep;
    }

    public ImmutableEndpoint getEndpoint() {
        return endpoint;
    }

    public void dispose() {
    }

    public void initialise()
        throws InitialisationException
    {
    }

    public void setMuleContext(MuleContext context) {
        muleContext = context;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public Object transform(Object src, MuleEvent event)
        throws TransformerMessagingException
    {
        return transform(src, null, event);
    }

    public Object transform(Object src, String encoding, MuleEvent event)
        throws TransformerMessagingException
    {
        if (src == null) {
            return null;
        }
        LocationBeanExpressionHolder holder = ((LocationBeanExpressionHolder) src);
        LocationBean result = new LocationBean();
        try {
            final Map _transformedAdditionalProperties = ((Map) evaluateAndTransform(this.muleContext, event, LocationBeanExpressionHolder.class.getDeclaredField("_additionalPropertiesType").getGenericType(), null, holder.getAdditionalProperties()));
            result.setAdditionalProperties(_transformedAdditionalProperties);
            final String _transformedDirections = ((String) evaluateAndTransform(this.muleContext, event, LocationBeanExpressionHolder.class.getDeclaredField("_directionsType").getGenericType(), null, holder.getDirections()));
            result.setDirections(_transformedDirections);
            final Long _transformedLocationId = ((Long) evaluateAndTransform(this.muleContext, event, LocationBeanExpressionHolder.class.getDeclaredField("_locationIdType").getGenericType(), null, holder.getLocationId()));
            result.setLocationId(_transformedLocationId);
            final String _transformedLocationName = ((String) evaluateAndTransform(this.muleContext, event, LocationBeanExpressionHolder.class.getDeclaredField("_locationNameType").getGenericType(), null, holder.getLocationName()));
            result.setLocationName(_transformedLocationName);
            final Long _transformedRegionId = ((Long) evaluateAndTransform(this.muleContext, event, LocationBeanExpressionHolder.class.getDeclaredField("_regionIdType").getGenericType(), null, holder.getRegionId()));
            result.setRegionId(_transformedRegionId);
            final String _transformedTimeZone = ((String) evaluateAndTransform(this.muleContext, event, LocationBeanExpressionHolder.class.getDeclaredField("_timeZoneType").getGenericType(), null, holder.getTimeZone()));
            result.setTimeZone(_transformedTimeZone);
            final ArrayOfXsdString _transformedInterviewRooms = ((ArrayOfXsdString) evaluateAndTransform(this.muleContext, event, LocationBeanExpressionHolder.class.getDeclaredField("_interviewRoomsType").getGenericType(), null, holder.getInterviewRooms()));
            result.setInterviewRooms(_transformedInterviewRooms);
            final ArrayOfXsdLong _transformedDefaultApprovers = ((ArrayOfXsdLong) evaluateAndTransform(this.muleContext, event, LocationBeanExpressionHolder.class.getDeclaredField("_defaultApproversType").getGenericType(), null, holder.getDefaultApprovers()));
            result.setDefaultApprovers(_transformedDefaultApprovers);
            final String _transformedCity = ((String) evaluateAndTransform(this.muleContext, event, AddressEntityBeanExpressionHolder.class.getDeclaredField("_cityType").getGenericType(), null, holder.getCity()));
            result.setCity(_transformedCity);
            final String _transformedAddress = ((String) evaluateAndTransform(this.muleContext, event, AddressEntityBeanExpressionHolder.class.getDeclaredField("_addressType").getGenericType(), null, holder.getAddress()));
            result.setAddress(_transformedAddress);
            final String _transformedZipCode = ((String) evaluateAndTransform(this.muleContext, event, AddressEntityBeanExpressionHolder.class.getDeclaredField("_zipCodeType").getGenericType(), null, holder.getZipCode()));
            result.setZipCode(_transformedZipCode);
            final String _transformedState = ((String) evaluateAndTransform(this.muleContext, event, AddressEntityBeanExpressionHolder.class.getDeclaredField("_stateType").getGenericType(), null, holder.getState()));
            result.setState(_transformedState);
            final String _transformedCountry = ((String) evaluateAndTransform(this.muleContext, event, AddressEntityBeanExpressionHolder.class.getDeclaredField("_countryType").getGenericType(), null, holder.getCountry()));
            result.setCountry(_transformedCountry);
            final String _transformedPhone = ((String) evaluateAndTransform(this.muleContext, event, AddressEntityBeanExpressionHolder.class.getDeclaredField("_phoneType").getGenericType(), null, holder.getPhone()));
            result.setPhone(_transformedPhone);
            final XMLGregorianCalendar _transformedCreationDate = ((XMLGregorianCalendar) evaluateAndTransform(this.muleContext, event, EntityBeanExpressionHolder.class.getDeclaredField("_creationDateType").getGenericType(), null, holder.getCreationDate()));
            result.setCreationDate(_transformedCreationDate);
            final ArrayOfFlexFieldBean _transformedFlexValues = ((ArrayOfFlexFieldBean) evaluateAndTransform(this.muleContext, event, EntityBeanExpressionHolder.class.getDeclaredField("_flexValuesType").getGenericType(), null, holder.getFlexValues()));
            result.setFlexValues(_transformedFlexValues);
            final Long _transformedId = ((Long) evaluateAndTransform(this.muleContext, event, EntityBeanExpressionHolder.class.getDeclaredField("_idType").getGenericType(), null, holder.getId()));
            result.setId(_transformedId);
            final XMLGregorianCalendar _transformedLastUpdated = ((XMLGregorianCalendar) evaluateAndTransform(this.muleContext, event, EntityBeanExpressionHolder.class.getDeclaredField("_lastUpdatedType").getGenericType(), null, holder.getLastUpdated()));
            result.setLastUpdated(_transformedLastUpdated);
            final String _transformedStatus = ((String) evaluateAndTransform(this.muleContext, event, EntityBeanExpressionHolder.class.getDeclaredField("_statusType").getGenericType(), null, holder.getStatus()));
            result.setStatus(_transformedStatus);
        } catch (NoSuchFieldException e) {
            throw new TransformerMessagingException(CoreMessages.createStaticMessage("internal error"), event, this, e);
        } catch (TransformerException e) {
            throw new TransformerMessagingException(e.getI18nMessage(), event, this, e);
        }
        return result;
    }

    public MuleEvent process(MuleEvent event) {
        return null;
    }

    public String getMimeType() {
        return null;
    }

    public String getEncoding() {
        return null;
    }

    public MuleContext getMuleContext() {
        return muleContext;
    }

}