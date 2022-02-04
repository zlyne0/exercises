import React from 'react'
import {
    Formik,
    FormikHelpers,
    FormikProps,
    Form,
    Field,
    FieldProps,
    ErrorMessage
} from 'formik';

import * as Yup from 'yup';
//import * as styles from './proposal.css'
import './proposal.css';
import { FAddress, FAddressValidationSchema } from './FAddress';
import { Proposal, Address, Person } from './model'
import {FPesel} from "./FPesel";


export interface FProposalProps {
    name: String
}

export class FProposal extends React.Component<FProposalProps> {

    constructor(props: FProposalProps) {
        super(props)
        this.state = {}
    }

    render() {
        const initialValues: Proposal = {
            person: { name: "Jan", surname: "Kowalski", email: '', pesel: '' },
            address: { street: "", city: ""}
        };

        const validationSchema = Yup.object({
            person: Yup.object({
                name: Yup.string().max(15, 'Must be 15 characters or less').required('Required'),
                surname: Yup.string().max(20, 'Must be 20 characters or less').required('Required'),
                email: Yup.string().email('Invalid email address').required('Required')
            }),
            address: FAddressValidationSchema
        });

        return (
            <div>
                <h1>Proposal: {this.props.name}</h1>
                <Formik
                    initialValues={initialValues}
                    validationSchema={validationSchema}
                    onSubmit={(values, actions) => {
                        console.log({ values, actions });
                        alert(JSON.stringify(values, null, 2));
                        actions.setSubmitting(false);
                    }}
                >
                    <Form>
                        <label htmlFor="person.name">Imię</label>
                        <Field id="person.name" name="person.name" placeholder="Imię" />
                        <ErrorMessage name="person.name"/>
                        <br/>

                        <label htmlFor="person.surname">Nazwisko</label>
                        <Field id="person.surname" name="person.surname" placeholder="Nazwisko"/>
                        <ErrorMessage name="person.surname" />
                        <br/>

                        <FPesel namespace="person.pesel" />
                        <br/>


                        <label htmlFor="person.email">Email</label>
                        <Field id="person.email" name="person.email" placeholder="Email"/>
                        <ErrorMessage component="span" name="person.email" className="formErrorMessage" />
                        <br/>

                        Address section<br/>
                        <FAddress namespace="address"/>

                        <button type="submit">Wyślij</button>

                    </Form>
                </Formik>
            </div>
        );
    }
}
