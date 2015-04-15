# Introduction #

PROMNeT++, although fully automated, needs some "guidance" from its user, in the form of annotated comments. This page describes the annotations used in PROMNeT++, and their semantics in particular.

# Annotation list #

| **Annotation** | **Parameters and values** | **Required** | **Description and usage notes** | **Example** |
|:---------------|:--------------------------|:-------------|:--------------------------------|:------------|
| @UsesTemplate | name (value=String) | Yes | Gives PROMNeT++ the indication to use the C++ code template named _name_. **Must be the very first annotation in your model.** Currently, the only supported template is "round\_based\_protocol\_generic", as PROMNeT++ was designed for Round-Based Consensus Protocols. | @UsesTemplate(name="round\_based\_protocol\_generic") |
| @TemplateParameter | name (value=String) | Not always | Makes it so that the value of the next _#define_ directive is treated as a parameter for the template. **Must be placed immediately before a #define directive.** Naturally, if no template has been specified (via @UsesTemplate), this fails. | @TemplateParameter(name="numberOfParticipants")<br>#define N 3<br><br>The above makes it so that, if using the "round_based_protocol_generic" template, the number of participants is 3.<br>
<tr><td> @BeginTemplateBlock </td><td> name (value=String) </td><td> Yes, depending on the template </td><td> Signals PROMNeT++ that anything below this annotation is part of a template block. <b>Must be used in conjunction with @EndTemplateBlock.</b> Any code pertaining to a template block is handled/translated differently from regular code. </td><td> @BeginTemplateBlock(name="generic_part") </td></tr>
<tr><td> @EndTemplateBlock </td><td> None </td><td> Yes, for every @BeginTemplateBlock </td><td> Closes the template block initiated by @BeginTemplateBlock. <b>Must be used in conjunction with @BeginTemplateBlock.</b> Any code pertaining to a template block is handled/translated differently from regular code. </td><td> @EndTemplateBlock </td></tr></tbody></table>

<h1>Templates using these annotations</h1>

As of this guide's time of writing, there's only one template, for Round-Based Consensus Protocols. Developers who re-use PROMNeT++'s code, or use PROMNeT++ as basis for their work, are encouraged to make it so that their tool works on different types of protocols.<br>
<br>
<h2>Template list</h2>

<ul><li><a href='https://code.google.com/p/promnetpp/wiki/Template_RoundBasedProtocolGeneric'>"round_based_protocol_generic"</a> (Implementation: RoundBasedProtocolGeneric.java)