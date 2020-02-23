import React, { Component } from 'react';
import axios from 'axios';

import Device from './../../components/Device/Device'
import './Devices.css';
import Modal from './../../components/Modal/Modal';

class Devices extends Component {
    state = {
        posts: [],
        error: false,
        purchasing: false,
    }

    componentDidMount() {
        axios.get('http://137.116.127.90:8080/streamservice/api/insurance/get-details')
            .then(response => {
                const posts = response.data;
                const updatedPosts = posts.map(post => {
                    return {
                        ...post,
                    }
                });
                this.setState({ posts: updatedPosts });
                // console.log( response );
            })
            .catch(error => {
                // console.log(error);
                this.setState({ error: true });
            });
    }

    postSelectedHandler = (id) => {
        this.setState({selectedPostId: id});
        this.setState({ purchasing: true });
    }

    render() {
        let posts = <p style={{ textAlign: 'center' }}>Something went wrong!</p>;
        if (!this.state.error) {
            posts = this.state.posts.map(post => {
                return <Device 
                    key={post._id}
                    deviceId={post._id}
                    userName={post.userName}
                />;
            });
        }
        return (
            <div className="Posts">
                <Modal show={this.state.purchasing} post={this.state.selectedPostId} />
                <p>{posts}</p>            
            </div>
        );
    }
}

export default Devices;

/*

<Modal show={this.state.purchasing}>
                    <OrderSummary ingredients={this.state.ingredients} />
                </Modal>

*/