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


	<xsl:template match="//property[@name='installerName']">
		<property name="installerName">
			<string><![CDATA[FCAT]]></string>
		</property>
	</xsl:template>

	<xsl:template match="//property[@name='smallIconName']">
		<property name="smallIconName">
			<string><![CDATA[FCAT-16.gif]]></string>
		</property>
	</xsl:template>
	<xsl:template match="//property[@name='largeIconName']">
		<property name="largeIconName">
			<string><![CDATA[FCAT-32.gif]]></string>
		</property>
	</xsl:template>
	<xsl:template match="//property[@name='macOSXIconName']">
		<property name="macOSXIconName">
			<string><![CDATA[FCAT.icns]]></string>
		</property>
	</xsl:template>

	<xsl:template match="//object[@objectID='c066c8048d0a']">
		<object class="com.zerog.ia.installer.util.VariablePropertyData" objectID="c066c8048d0a">
			<property name="propertyValue">
				<string><![CDATA[true]]></string>
			</property>
			<property name="propertyName">
				<string><![CDATA[InstallOnlineAsmtFCAT]]></string>
			</property>
		</object>
	</xsl:template>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
		
</xsl:stylesheet>