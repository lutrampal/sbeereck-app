import React from 'react';
import { TouchableOpacity, View, Text } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import styles from './styles';

export default class Header extends React.Component {
    render() {
        return (
            <TouchableOpacity onPress={() => { this.props.onItemClick(this.props.item) }} style={styles.bottomContainer}>
                <View style={styles.leftSide}>
                    <Text style={styles.name}>{this.props.item.name}</Text>
                    <Text style={styles.sbeers}>{this.props.item.number_of_attendees} participants</Text>
                    <Text style={styles.benefits}>{this.props.item.balance} €</Text>
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