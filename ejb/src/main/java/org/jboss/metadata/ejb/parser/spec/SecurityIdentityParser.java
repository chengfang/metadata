package org.jboss.metadata.ejb.parser.spec;

import org.jboss.metadata.ejb.spec.SecurityIdentityMetaData;
import org.jboss.metadata.javaee.spec.EmptyMetaData;
import org.jboss.metadata.javaee.spec.RunAsMetaData;
import org.jboss.metadata.javaee.support.IdMetaData;
import org.jboss.metadata.parser.ee.RunAsMetaDataParser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static org.jboss.metadata.ejb.parser.spec.AttributeProcessorHelper.processAttributes;
import org.jboss.metadata.property.PropertyReplacer;
import org.jboss.metadata.property.PropertyReplacers;

/**
 * Parses the &lt;security-identity&gt; element in a ejb-jar.xml
 *
 * User: Jaikiran Pai
 */
public class SecurityIdentityParser extends AbstractWithDescriptionsParser<SecurityIdentityMetaData>
{
   private static final AttributeProcessor<IdMetaData> ATTRIBUTE_PROCESSOR = new IdMetaDataAttributeProcessor<IdMetaData>(UnexpectedAttributeProcessor.instance());
   public static final SecurityIdentityParser INSTANCE = new SecurityIdentityParser();

   @Override
   public SecurityIdentityMetaData parse(XMLStreamReader reader, final PropertyReplacer propertyReplacer) throws XMLStreamException
   {
      final SecurityIdentityMetaData securityIdentity = new SecurityIdentityMetaData();
      processAttributes(securityIdentity, reader, ATTRIBUTE_PROCESSOR);
      this.processElements(securityIdentity, reader, propertyReplacer);
      return securityIdentity;
   }

   @Override
   protected void processElement(final SecurityIdentityMetaData metaData, final XMLStreamReader reader, final PropertyReplacer propertyReplacer) throws XMLStreamException
   {
      final EjbJarElement ejbJarElement = EjbJarElement.forName(reader.getLocalName());
      switch (ejbJarElement)
      {
         case USE_CALLER_IDENTITY:
            // read away the emptiness
            reader.getElementText();
            metaData.setUseCallerIdentity(new EmptyMetaData());
            return;

         case RUN_AS:
            final RunAsMetaData runAs = RunAsMetaDataParser.parse(reader, propertyReplacer);
            metaData.setRunAs(runAs);
            return;
         
         default:
            super.processElement(metaData, reader, propertyReplacer);

      }
   }
}
