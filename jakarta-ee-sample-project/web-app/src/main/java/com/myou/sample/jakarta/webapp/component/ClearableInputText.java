package com.myou.sample.jakarta.webapp.component;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Optional;

@FacesComponent(createTag = true, tagName = "clearableInputText", namespace = "http://sample.myou.com/jsf")
public class ClearableInputText extends HtmlInputText {

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("span", this);
        writer.writeAttribute("class", "clearable", "class");

        String onblur = "hideClearButton(this);" +  Optional.ofNullable(getOnblur()).map(s -> " " + s).orElse("");
        String onfocus = "showClearButton(this);" + Optional.ofNullable(getOnfocus()).map(s -> " " + s).orElse("");
        String onkeydown =  "showClearButton(this);" +  Optional.ofNullable(getOnkeydown()).map(s -> " " + s).orElse("");
        setOnblur(onblur);
        setOnfocus(onfocus);
        setOnkeydown(onkeydown);

        // Delegate to HtmlInputText to render the input element
        super.encodeBegin(context);
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        super.encodeEnd(context);

        ResponseWriter writer = context.getResponseWriter();

        // Write the clear button
        writer.startElement("span", this);
        writer.writeAttribute("class", "clearable__clear", "class");
        writer.writeAttribute("onclick", "clearInput(this)", null);
        writer.write("&times;");
        writer.endElement("span");

        writer.endElement("span");

    }
}
