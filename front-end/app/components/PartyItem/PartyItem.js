import React from 'react';
import { TouchableOpacity, View, Text } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import styles from './styles';
import { Col, Row, Grid } from "react-native-easy-grid";

export default class Header extends React.Component {
    render() {
        return (
            <TouchableOpacity onPress={() => { this.props.onItemClick(this.props.item) }} style={styles.bottomContainer}>
                <View style={styles.leftSide}>
                    <Text style={styles.name}>{this.props.item.name}</Text>
                    <Text style={styles.sbeers}>{this.props.item.number_of_attendees} participants</Text>
                    <Grid>
                        <Row>
                            <Col><Text>Dépots</Text></Col>
                            <Col><Text>Retraits</Text></Col>
                            <Col><Text>Balance</Text></Col>
                        </Row>
                        <Row>
                            <Col><Text>{this.props.item.topup.toFixed(2)} €</Text></Col>
                            <Col><Text>{this.props.item.revenue.toFixed(2)} €</Text></Col>
                            <Col><Text>{(this.props.item.topup + this.props.item.revenue).toFixed(2)} €</Text></Col>
                        </Row>
                    </Grid>
                </View>
                <View style={styles.rightSide}>
                    <Text style={styles.date}>{this.props.item.date}</Text>
                    <TouchableOpacity onPress={() => {this.props.onEditClick(this.props.item)}} style={styles.editButton}>
                        <Icon
                            color="white"
                            name="pencil"
                            size={18}
                        />
                    </TouchableOpacity>
                </View>
            </TouchableOpacity>
        );
    }
}