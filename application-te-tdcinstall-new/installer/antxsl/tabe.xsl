<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="string[.='']">
		<string><![CDATA[@@EMPTY.STRING@@]]></string>
	</xsl:template>

	<xsl:template match="//property[@name='productVersionMajor']">
		<property name="productVersionMajor">
			<int>@@PRODUCT.VERSION.MAJOR@@</int>
		</property>
	</xsl:template>
	<xsl:template match="//property[@name='productVersionMinor']">
		<property name="productVersionMinor">
			<int>@@PRODUCT.VERSION.MINOR@@</int>
		</property>
	</xsl:template>
	<xsl:template match="//property[@name='productVersionRevision']">
		<property name="productVersionRevision">
			<int>@@PRODUCT.VERSION.REVISION@@</int>
		</property>
	</xsl:template>


	<xsl:template match="//object[@objectID='8398e5c5bd63']">
		<object class="com.zerog.ia.installer.util.VariablePropertyData" objectID="8398e5c5bd63">
			<property name="propertyValue">
				<string><![CDATA[TABE]]></string>
			</property>
			<property name="propertyName">
				<string><![CDATA[$PRODUCT_TYPE$]]></string>
			</property>
		</object>
	</xsl:template>
 
	<xsl:template match="//object[@objectID='59ea395d9789']">
		<object class="com.zerog.ia.installer.util.VariablePropertyData" objectID="59ea395d9789">
			<property name="propertyValue">
				<string><![CDATA[true]]></string>
			</property>
			<property name="propertyName">
				<string><![CDATA[$TABE$]]></string>
			</property>
		</object>
	</xsl:template>
 
 	<xsl:template match="//property[@name='installerName']">
		<property name="installerName">
			<string><![CDATA[InstallOAS_TABE]]></string>
		</property>
	</xsl:template>
    
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
		
</xsl:stylesheet>