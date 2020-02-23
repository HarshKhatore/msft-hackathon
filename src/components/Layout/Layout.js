import React, { Component } from 'react';

import classes from './Layout.css';
import Toolbar from '../Navigation/Toolbar/Toolbar';

class Layout extends Component {
    state = {
        showSideDrawer: false
    }

    sideDrawerClosedHandler = () => {
        this.setState( { showSideDrawer: false } );
    }

    sideDrawerToggleHandler = () => {
        this.setState( ( prevState ) => {
            return { showSideDrawer: !prevState.showSideDrawer };
        } );
    }

    render () {
        return (
            <div>
                <Toolbar drawerToggleClicked={this.sideDrawerToggleHandler} />
                <main className={classes.Content}>
                    {this.props.children}
                </main>
            </div>
        )
    }
}

export default Layout;