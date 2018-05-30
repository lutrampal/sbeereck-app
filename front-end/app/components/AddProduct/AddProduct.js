import React from 'react';
import PropTypes from 'prop-type';
import { TouchableOpacity, View, Text, TextInput, Keyboard, Picker } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import styles from './styles';
import {SelectNumber} from '../SelectNumber';

export default class Header extends React.Component {
    render() {
        return (
            <View style={{flex: 1}}>
                <Text style={styles.addTitle}>Ajouter un produit</Text>
                <TextInput
                    style={styles.addName}
                    autoCorrect={false}
                    onChangeText={this.props.onChangeProductName}
                    value={this.props.newProductName}
                    placeholder="Nom"
                    onEndEditing={() => { Keyboard.dismiss }}
                />
                <SelectNumber
                    style={styles.addPrice}
                    price={true}
                    priceValue={this.props.priceValue}
                    onChangeText={this.props.onChangePrice}
                    onMorePress={this.props.onMorePress}
                    onLessPress={this.props.onLessPress} />
                <Picker
                    style={styles.addType}
                    selectedValue={this.props.newProductType}
                    onValueChange={(value, index) => this.props.onTypeChange(value)}>
                    <Picker.Item label="Bière" value="beer" />
                    <Picker.Item label="Caution" value="deposit" />
                    <Picker.Item label="Nourriture" value="food" />
                </Picker>
                <TouchableOpacity onPress={this.props.onValidatePress} style={styles.validateButton}>
                    <Text style={styles.validateText}>Sauvegarder</Text>
                </TouchableOpacity>
            </View>
        );
    }
}