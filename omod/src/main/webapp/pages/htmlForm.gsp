${ ui.includeFragment(fragmentProvider, fragmentName, [
        patient: patient,
        encounter: encounter,
        definitionUiResource: 'imbemr:htmlforms/' + formName + '.xml',
        returnUrl: returnUrl
]) }