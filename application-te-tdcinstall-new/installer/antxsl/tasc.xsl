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

	<xsl:template match="//object[@objectID='db2190d38669']">
		<object class="com.zerog.ia.installer.util.VariablePropertyData" objectID="db2190d38669">
			<property name="propertyValue">
				<string><![CDATA[TASC]]></string>
			</property>
			<property name="propertyName">
				<string><![CDATA[$PRODUCT_TYPE$]]></string>
			</property>
		</object>
	</xsl:template>

	<xsl:template match="//object[@objectID='db2190d3866b']">
		<object class="com.zerog.ia.installer.util.VariablePropertyData" objectID="db2190d3866b">
			<property name="propertyValue">
				<string><![CDATA[true]]></string>
			</property>
			<property name="propertyName">
				<string><![CDATA[$TASC$]]></string>
			</property>
		</object>
	</xsl:template>

	<xsl:template match="//property[@name='installerName']">
		<property name="installerName">
			<string><![CDATA[InstallOAS_TASC_DEF]]></string>
		</property>
	</xsl:template>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
		
</xsl:stylesheet>