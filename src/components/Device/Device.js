import React from 'react';

import './Device.css';

const device = (props) => {

    return (
        <div className="Device">
            <h5>{props.deviceId}</h5>
            <p>{props.userName}</p>
        </div>
    );
}

export default device;