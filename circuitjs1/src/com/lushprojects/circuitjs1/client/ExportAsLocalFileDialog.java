/*    
    Copyright (C) Paul Falstad and Iain Sharp
    
    This file is part of CircuitJS1.

    CircuitJS1 is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License, or
    (at your option) any later version.

    CircuitJS1 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CircuitJS1.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.lushprojects.circuitjs1.client;

import java.util.Date;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.i18n.client.DateTimeFormat;

public class ExportAsLocalFileDialog extends DialogBox implements ValueChangeHandler<String> {
	
	VerticalPanel vp;
	
	static public final native boolean downloadIsSupported() 
	/*-{
		return !!(("download" in $doc.createElement("a")));
	 }-*/;
	
	static public final native String getBlobUrl(String data) 
	/*-{
		var datain=[""];
		datain[0]=data;
		var oldblob = $doc.exportBlob;
		if (oldblob)
		    URL.revokeObjectURL(oldblob);
		var blob=new Blob(datain, {type: 'text/plain' } );
		var url = URL.createObjectURL(blob);
		$doc.exportBlob = url;
		return url;
	}-*/;
	
	TextBox textBox;
	Anchor a;
	static String lastFileName;
	
	public ExportAsLocalFileDialog(String data) {
		super();
		Button okButton;
		String url;
		vp=new VerticalPanel();
		setWidget(vp);
		setText(CirSim.LS("Export as Local File"));
		vp.add(new Label(CirSim.LS("File name:")));
		textBox = new TextBox();
                textBox.addValueChangeHandler(this);
		textBox.setWidth("90%");
		vp.add(textBox);
		vp.add(new Label(CirSim.LS("Click on the link below to save your circuit")));
		url=getBlobUrl(data);
		Date date = new Date();
		String fname;
		if (lastFileName != null)
		    fname = lastFileName;
		else {
		    DateTimeFormat dtf = DateTimeFormat.getFormat("yyyyMMdd-HHmm");
		    fname = "circuit-"+ dtf.format(date) + ".circuitjs.txt";
		}
		textBox.setText(fname);
		a=new Anchor(fname, url);
		a.getElement().setAttribute("Download", fname);
		vp.add(a);
		vp.add(okButton = new Button(CirSim.LS("OK")));
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				closeDialog();
			}
		});
		this.center();
	}
	
	public void onValueChange(ValueChangeEvent<String> event) {
	    // update filename
	    String fname = textBox.getText();
	    if (fname.length() == 0)
		return;
	    if (!fname.contains("."))
		fname += ".txt";
	    a.getElement().setAttribute("Download", fname);
	    a.setText(fname);
	    lastFileName = fname;
	}
	
	protected void closeDialog()
	{
		this.hide();
	}

}
