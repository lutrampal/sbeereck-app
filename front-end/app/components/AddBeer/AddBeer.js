import React from 'react';
import PropTypes from 'prop-type';
import { TouchableOpacity, View, Text, TextInput, Keyboard, Picker } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import DatePicker from 'react-native-datepicker';
import styles from './styles';
import {SelectNumber} from '../SelectNumber';

export default class Header extends React.Component {
    render() {
        return (
            <View style={{flex: 1}}>
                <Text style={styles.addTitle}>Ajouter une bière</Text>
                <Picker
                    style={styles.addType}
                    selectedValue={this.props.selectedBeerAddId}
                    onValueChange={this.props.onBeerChange}>

                    {this.props.availableBeers.map((data) => {
                        return (
                            <Picker.Item label={data.label} value={data.value} />
                        )
                    })}
                </Picker>
                
                
                <TouchableOpacity onPress={this.props.onValidatePress} style={styles.validateButton}>
                    <Text style={styles.validateText}>Ajouter</Text>
                </TouchableOpacity>
            </View>
        );
    }
}