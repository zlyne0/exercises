import React from "react";
import * as Yup from "yup";
import {ErrorMessage, Field, Formik} from "formik";
import {Address} from "./model";

export interface FAddressProps {
    namespace: string
}

export const FAddressValidationSchema = Yup.object({
    street: Yup.string().max(15, 'Must be 15 characters or less').required('Required'),
    city: Yup.string().max(20, 'Must be 20 characters or less').required('Required')
});

export class FAddress extends React.Component<FAddressProps> {

    constructor(props: FAddressProps) {
        super(props)
        this.state = {}
    }

    withNamespace(fieldName: string) {
        const { namespace } = this.props
        return namespace ? `${namespace}.${fieldName}` : fieldName
    }

    render() {
        return (
            <React.Fragment>
                <label htmlFor={this.withNamespace("street")}>Ulica</label>
                <Field id={this.withNamespace("street")} name={this.withNamespace("street")} placeholder="Ulica"/>
                <ErrorMessage component="span" name={this.withNamespace("street")} className="formErrorMessage" /> <br/>

                <label htmlFor={this.withNamespace("city")}>Miasto</label>
                <Field id={this.withNamespace("city")} name={this.withNamespace("city")} placeholder="Miasto"/>
                <ErrorMessage component="span" name={this.withNamespace("city")} className="formErrorMessage" /> <br/>
            </React.Fragment>
        );
    }
}
