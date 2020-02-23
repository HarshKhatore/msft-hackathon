import React from 'react';
import logo from './logo.svg';
import './App.css';

import Devices from './containers/Devices/Devices';

function App() {
  return (
    <div>
      <h2 style={{textAlign: 'center'}}>Insurance Dashboard</h2>
      <Devices />

    </div>
  );
}

export default App;
