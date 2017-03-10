/*
 * Copyright (C) 2003-2014  Pascal Essiembre
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.essiembre.eclipse.rbe.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.essiembre.eclipse.rbe.RBEPlugin;
import com.essiembre.eclipse.rbe.model.workbench.RBEPreferences;

/**
 * Plugin preference page for reporting/performance options.
 * @author Pascal Essiembre
 * @author Wolfgang Schramm
 */
public class RBEReportingPrefPage extends AbstractRBEPrefPage {
    
    /* Preference fields. */
    private Combo reportMissingVals;
    private Combo reportDuplVals;
    private Combo reportSimVals;
    private Text reportSimPrecision;
    private Button[] reportSimValsMode = new Button[2];

    /**
     * Constructor.
     */
    public RBEReportingPrefPage() {
        super();
    }

    @Override
    protected Control createContents(Composite parent) {
        IPreferenceStore prefs = getPreferenceStore();
        Composite field = null;
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        
        new Label(composite, SWT.NONE).setText(
                RBEPlugin.getString("prefs.perform.intro1"));
        new Label(composite, SWT.NONE).setText(
                RBEPlugin.getString("prefs.perform.intro2"));
        new Label(composite, SWT.NONE).setText(" ");

        // Report missing values?
        field = createFieldComposite(composite);
    	GridData gridData = new GridData();
    	gridData.grabExcessHorizontalSpace = true;
    	field.setLayoutData(gridData);
        new Label(field, SWT.NONE).setText(
        		RBEPlugin.getString("prefs.perform.missingVals")); //$NON-NLS-1$
        reportMissingVals = new Combo(field, SWT.READ_ONLY);
        populateCombo(reportMissingVals,
        		prefs.getInt(RBEPreferences.REPORT_MISSING_VALUES_LEVEL));

        // Report duplicate values?
        field = createFieldComposite(composite);
    	gridData = new GridData();
    	gridData.grabExcessHorizontalSpace = true;
    	field.setLayoutData(gridData);
        new Label(field, SWT.NONE).setText(
        		RBEPlugin.getString("prefs.perform.duplVals")); //$NON-NLS-1$
        reportDuplVals = new Combo(field, SWT.READ_ONLY);
        populateCombo(reportDuplVals,
        		prefs.getInt(RBEPreferences.REPORT_DUPL_VALUES_LEVEL));
        
        // Report similar values?
        field = createFieldComposite(composite);
    	gridData = new GridData();
    	gridData.grabExcessHorizontalSpace = true;
    	field.setLayoutData(gridData);

        new Label(field, SWT.NONE).setText(
        		RBEPlugin.getString("prefs.perform.simVals")); //$NON-NLS-1$
        reportSimVals = new Combo(field, SWT.READ_ONLY);
        populateCombo(reportSimVals,
        		prefs.getInt(RBEPreferences.REPORT_SIM_VALUES_LEVEL));
        reportSimVals.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                refreshEnabledStatuses();
            }
        });
        
        Composite simValModeGroup = new Composite(composite, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = indentPixels;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        simValModeGroup.setLayout(gridLayout);
        
        // Report similar values: word count
        reportSimValsMode[0] = new Button(simValModeGroup, SWT.RADIO);
        reportSimValsMode[0].setSelection(prefs.getBoolean(
                RBEPreferences.REPORT_SIM_VALUES_WORD_COMPARE));
        new Label(simValModeGroup, SWT.NONE).setText(RBEPlugin.getString(
                "prefs.perform.simVals.wordCount"));
        
        // Report similar values: Levensthein
        reportSimValsMode[1] = new Button(simValModeGroup, SWT.RADIO);
        reportSimValsMode[1].setSelection(prefs.getBoolean(
                RBEPreferences.REPORT_SIM_VALUES_LEVENSTHEIN));
        new Label(simValModeGroup, SWT.NONE).setText(RBEPlugin.getString(
                "prefs.perform.simVals.levensthein"));
        
        // Report similar values: precision level
        field = createFieldComposite(composite, indentPixels);
        new Label(field, SWT.NONE).setText(RBEPlugin.getString(
                "prefs.perform.simVals.precision"));
        reportSimPrecision = new Text(field, SWT.BORDER);
        reportSimPrecision.setText(
                prefs.getString(RBEPreferences.REPORT_SIM_VALUES_PRECISION));
        reportSimPrecision.setTextLimit(6);
        setWidthInChars(reportSimPrecision, 6);
        reportSimPrecision.addKeyListener(new DoubleTextValidatorKeyListener(
                RBEPlugin.getString(
                        "prefs.perform.simVals.precision.error"),
                0, 1));
        
        refreshEnabledStatuses();
        
        return composite;
    }


    /**
     * Creates the items in the combo and select the item that matches the
     * current value.
     * @param combo
     * @param selectedLevel
     */
    private void populateCombo(Combo combo, int selectedLevel) {
    	
    	combo.add(RBEPlugin.getString("prefs.perform.message.ignore"));
    	combo.add(RBEPlugin.getString("prefs.perform.message.info"));
    	combo.add(RBEPlugin.getString("prefs.perform.message.warning"));
    	combo.add(RBEPlugin.getString("prefs.perform.message.error"));
    	
    	combo.select(selectedLevel);
    	
    	GridData gridData = new GridData();
    	gridData.grabExcessHorizontalSpace = true;
    	gridData.horizontalAlignment = SWT.RIGHT;
    	combo.setLayoutData(gridData);
    }
    
    
    @Override
    public boolean performOk() {
        IPreferenceStore prefs = getPreferenceStore();
        prefs.setValue(RBEPreferences.REPORT_MISSING_VALUES_LEVEL, reportMissingVals.getSelectionIndex());
        prefs.setValue(RBEPreferences.REPORT_DUPL_VALUES_LEVEL, reportDuplVals.getSelectionIndex());
        prefs.setValue(RBEPreferences.REPORT_SIM_VALUES_LEVEL, reportSimVals.getSelectionIndex());
        prefs.setValue(RBEPreferences.REPORT_SIM_VALUES_WORD_COMPARE,
                reportSimValsMode[0].getSelection());
        prefs.setValue(RBEPreferences.REPORT_SIM_VALUES_LEVENSTHEIN,
                reportSimValsMode[1].getSelection());
        prefs.setValue(RBEPreferences.REPORT_SIM_VALUES_PRECISION,
                Double.parseDouble(reportSimPrecision.getText()));
        refreshEnabledStatuses();
        return super.performOk();
    }
    
    
    @Override
    protected void performDefaults() {
        IPreferenceStore prefs = getPreferenceStore();
        reportMissingVals.select(prefs.getDefaultInt(RBEPreferences.REPORT_MISSING_VALUES_LEVEL));
        reportDuplVals.select(prefs.getDefaultInt(RBEPreferences.REPORT_DUPL_VALUES_LEVEL));
        reportSimVals.select(prefs.getDefaultInt(RBEPreferences.REPORT_SIM_VALUES_LEVEL));
        reportSimValsMode[0].setSelection(prefs.getDefaultBoolean(
                RBEPreferences.REPORT_SIM_VALUES_WORD_COMPARE));
        reportSimValsMode[1].setSelection(prefs.getDefaultBoolean(
                RBEPreferences.REPORT_SIM_VALUES_LEVENSTHEIN));
        reportSimPrecision.setText(Double.toString(prefs.getDefaultDouble(
                RBEPreferences.REPORT_SIM_VALUES_PRECISION)));
        refreshEnabledStatuses();
        super.performDefaults();
    }

    /*default*/ void refreshEnabledStatuses() {
    	
        boolean isReportingSimilar = reportSimVals.getSelectionIndex() != RBEPreferences.VALIDATION_MESSAGE_IGNORE;

        for (int i = 0; i < reportSimValsMode.length; i++) {
            reportSimValsMode[i].setEnabled(isReportingSimilar);
        }
        reportSimPrecision.setEnabled(isReportingSimilar);
    }
    
}
