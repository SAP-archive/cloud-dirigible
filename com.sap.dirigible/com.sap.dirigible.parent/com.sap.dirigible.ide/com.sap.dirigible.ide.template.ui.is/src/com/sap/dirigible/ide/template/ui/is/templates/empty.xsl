<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
    xmlns:m="...custom namespace of your service...">
    
<!-- DEFAULT EMPTY IMPLEMENTATION FOR PAYLOAD TRANSFORMATION -->
    
    <xsl:template match="/">
        <xsl:apply-templates select="soap:Envelope/soap:Body/*"/>
    </xsl:template>
    
    <xsl:template match="@*|node()">
        ... actual transformation goes here ...
    </xsl:template>
</xsl:stylesheet>
