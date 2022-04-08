import React from 'react';
import logo from './logo.svg';
import './App.css';
import { FProposal, FProposalProps } from './proposal/FProposal'
import { FChat } from './chat/FChat';

function App() {
  return (
    <div className="App">
        <FChat nickname="Hari Seldon"/>
    </div>
  );
}

export default App;
