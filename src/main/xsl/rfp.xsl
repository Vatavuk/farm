<?xml version="1.0"?>
<!--
Copyright (c) 2016-2018 Zerocracy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to read
the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" version="2.0">
  <xsl:output method="html" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes"/>
  <xsl:strip-space elements="*"/>
  <xsl:include href="/xsl/inner-layout.xsl"/>
  <xsl:template match="page" mode="head">
    <title>
      <xsl:text>RFP</xsl:text>
    </title>
    <xsl:apply-templates select="." mode="js"/>
  </xsl:template>
  <xsl:template match="page" mode="inner">
    <h1>
      <xsl:text>RFP</xsl:text>
    </h1>
    <xsl:if test="not(rfp)">
      <form id="form" style="display:none" action="/rfp-pay" method="post">
        <input name="cents" id="cents" type="hidden"/>
        <input name="token" id="token" type="hidden"/>
        <input name="email" id="email" type="hidden"/>
        <input type="submit"/>
      </form>
      <p>
        <xsl:text>If you are ready to outsource your software project</xsl:text>
        <xsl:text> to a team managed by Zerocracy, this is the best place</xsl:text>
        <xsl:text> to start. First of all, you will need a software architect,</xsl:text>
        <xsl:text> who knows how to work with Zerocrat, who understands</xsl:text>
        <xsl:text> our management principles and has enough experience</xsl:text>
        <xsl:text> and reputation. The full list of programmers is </xsl:text>
        <a href="/team">
          here
        </a>
        <xsl:text>. You need to make one of them interested in your project.</xsl:text>
        <xsl:text> Once you hire that guy, he/she will help you recruit</xsl:text>
        <xsl:text> the entire team and will technically manage it, with</xsl:text>
        <xsl:text> the help of Zerocrat.</xsl:text>
        <xsl:text> You need one of the best, with the highest reputation.</xsl:text>
        <xsl:text> Once you pay us the entrance fee below, we let you</xsl:text>
        <xsl:text> fill out and submit the form.</xsl:text>
        <xsl:text> Then, we will send your RFP to the best developers in the list.</xsl:text>
        <xsl:text> One of them will</xsl:text>
        <xsl:text> get back to you by email and you will discuss the next steps.</xsl:text>
      </p>
      <p>
        <xsl:text>Pay </xsl:text>
        <span style="text-decoration: line-through">
          <xsl:text>$64.00</xsl:text>
        </span>
        <xsl:text> </xsl:text>
        <a href="#" class="pay" data-cents="1600">
          <xsl:text>$16.00</xsl:text>
        </a>
        <xsl:text> to continue. The payment is not refundable!</xsl:text>
      </p>
    </xsl:if>
    <xsl:apply-templates select="rfp"/>
    <form action="/rfp-post" method="post">
      <fieldset>
        <label>
          <xsl:text>Statement of work (</xsl:text>
          <xsl:text>please, keep it as short as this input area;</xsl:text>
          <xsl:text> don't use any formatting or HTML, just plain text in one paragraph</xsl:text>
          <xsl:text>):</xsl:text>
        </label>
        <textarea name="sow" style="width:100%;height:10em;">
          <xsl:choose>
            <xsl:when test="rfp">
              <xsl:value-of select="rfp/sow"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="disabled">
                <xsl:text>disabled</xsl:text>
              </xsl:attribute>
              <xsl:text>Project description goes here...</xsl:text>
            </xsl:otherwise>
          </xsl:choose>
        </textarea>
        <button>
          <xsl:choose>
            <xsl:when test="rfp">
              <xsl:attribute name="type">
                <xsl:text>submit</xsl:text>
              </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="disabled">
                <xsl:text>disabled</xsl:text>
              </xsl:attribute>
            </xsl:otherwise>
          </xsl:choose>
          <xsl:text>Update</xsl:text>
        </button>
      </fieldset>
    </form>
    <p>
      <xsl:text>Make sure the Statement of Work is short, </xsl:text>
      <xsl:text> easy to understand, and doesn't contain your contact information.</xsl:text>
      <xsl:text> This is for your own good, since in order to get</xsl:text>
      <xsl:text> in touch with you each programmer will have to sacrifice</xsl:text>
      <xsl:text> a decent amount of reputation points. This is how we</xsl:text>
      <xsl:text> filter out those who are not serious.</xsl:text>
    </p>
  </xsl:template>
  <xsl:template match="rfp">
    <p>
      <xsl:text>ID: #</xsl:text>
      <xsl:value-of select="id"/>
      <xsl:text> (it is visible in the </xsl:text>
      <a href="/rfps">
        <xsl:text>list</xsl:text>
      </a>
      <xsl:text>)</xsl:text>
    </p>
    <p>
      <xsl:text>Created: </xsl:text>
      <xsl:value-of select="created"/>
      <xsl:text>.</xsl:text>
    </p>
    <p>
      <xsl:text>Paid: </xsl:text>
      <code>
        <xsl:value-of select="paid"/>
      </code>
      <xsl:text>.</xsl:text>
    </p>
    <p>
      <xsl:text>Email: </xsl:text>
      <code>
        <xsl:value-of select="email"/>
      </code>
      <xsl:text>.</xsl:text>
    </p>
  </xsl:template>
  <xsl:template match="page" mode="js">
    <xsl:element name="script">
      <xsl:attribute name="src">
        <xsl:text>https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js</xsl:text>
      </xsl:attribute>
    </xsl:element>
    <xsl:element name="script">
      <xsl:attribute name="src">
        <xsl:text>https://checkout.stripe.com/checkout.js</xsl:text>
      </xsl:attribute>
    </xsl:element>
    <xsl:element name="script">
      <xsl:attribute name="type">
        <xsl:text>text/javascript</xsl:text>
      </xsl:attribute>
      <xsl:text>var stripe_key='</xsl:text>
      <xsl:value-of select="stripe_key"/>
      <xsl:text>';</xsl:text>
    </xsl:element>
    <xsl:element name="script">
      <xsl:attribute name="type">
        <xsl:text>text/javascript</xsl:text>
      </xsl:attribute>
      <xsl:text>
        // <![CDATA[
        $(function() {
          var handler = StripeCheckout.configure({
            key: stripe_key,
            image: 'http://www.zerocracy.com/logo.svg',
            token: function (token) {
              $('#token').val(token.id);
              $('#email').val(token.email);
              $('#form').submit();
            }
          });
          $('a.pay').on('click', function (e) {
            var cents = $(this).attr('data-cents')
            $('#cents').val(cents);
            handler.open({
              name: 'Pay for RFP',
              description: 'One-time RFP fee',
              amount: cents
            });
            e.preventDefault();
          });
          $(window).on('popstate', function () {
            handler.close();
          });
        });
        // ]]>
      </xsl:text>
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>
