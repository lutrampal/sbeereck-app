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
                <Text style={styles.addTitle}>{this.props.item.name}</Text>
                <TextInput
                    style={styles.addName}
                    autoCorrect={false}
                    onChangeText={(text) => this.props.onChangeProductName(text)}
                    value={this.props.productName}
                    placeholder="Nom"
                    onEndEditing={() => { Keyboard.dismiss }}
                />
                <SelectNumber
                    style={styles.addPrice}
                    price={true}
                    priceValue={this.props.productPrice.toString()}
                    onChangeText={(value) => this.props.onChangePrice(value)}
                    onMorePress={() => this.props.onMorePress()}
                    onLessPress={() => this.props.onLessPress()} />
                <Picker
                    style={styles.addType}
                    selectedValue={this.props.productType}
                    onValueChange={(value, index) => this.props.onTypeChange(value)}>
                    <Picker.Item label="Bière" value="beer" />
                    <Picker.Item label="Caution" value="deposit" />
                    <Picker.Item label="Nourriture" value="food" />
                </Picker>
                <TouchableOpacity onPress={() => this.props.onValidatePress(this.props.item)} style={styles.validateButton}>
                    <Text style={styles.validateText}>Sauvegarder</Text>
                </TouchableOpacity>
                <TouchableOpacity onPress={() => this.props.onSuppressPress(this.props.item)} style={styles.suppressButton}>
                    <Text style={styles.validateText}>Supprimer</Text>
                </TouchableOpacity>
            </View>
        );
    }
}