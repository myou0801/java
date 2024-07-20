package com.myou.sample.jakarta.webapp.component;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;

import java.io.IOException;

@FacesComponent(createTag = true, tagName = "clearableInputText", namespace = "http://sample.myou.com/jsf")
public class ClearableInputText extends HtmlInputText {

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = getClientId(context);

        // Start the outer div
        writer.startElement("span", this);
        writer.writeAttribute("class", "clearable", "class");

        // Delegate to HtmlInputText to render the input element
        super.encodeBegin(context);
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        super.encodeEnd(context);

        ResponseWriter writer = context.getResponseWriter();
        String clientId = getClientId(context);

        // Write the clear button
        writer.startElement("span", this);
        writer.writeAttribute("class", "clearable__clear", "class");
        writer.writeAttribute("onclick", "clearInput('" + clientId + "')", null);
        writer.write("&times;");
        writer.endElement("span");

        // Close the outer div
        writer.endElement("span");

    }
}
