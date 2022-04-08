import React from "react";
import * as Yup from "yup";
import {ErrorMessage, Field, Formik} from "formik";
import {FAddressProps} from "./FAddress";

export interface FPeselProps {
    namespace: string
}

export class FPesel extends React.Component<FPeselProps> {

    constructor(props: FPeselProps) {
        super(props)
        this.state = {}
    }

    peselValidator(value: string): string {
        let error: string
        error = ''
        if (value === undefined || value.length !== 3) {
            error = 'Invalid'
        }
        return error
    }

    withNamespace() {
        const { namespace } = this.props
        return `${namespace}`
    }

    render() {
        return (
            <React.Fragment>
                <label htmlFor={this.withNamespace()}>Pesel</label>
                <Field id={this.withNamespace()} name={this.withNamespace()} placeholder="Pesel" validate={this.peselValidator}/>
                <ErrorMessage component="span" name={this.withNamespace()} className="formErrorMessage" /> <br/>
            </React.Fragment>
        )
    }

}
