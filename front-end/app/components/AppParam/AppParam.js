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
            <View style={{width: '100%', height: 'auto', borderBottomWidth: 1, borderColor: 'grey', padding: 10}}>
                <Text style={styles.addTitle}>Paramètres</Text>
                <Text style={styles.category}>Limite de découvert</Text>
                <SelectNumber
                    style={styles.price}
                    price={false}
                    priceValue={this.props.balance_treshold}
                    onChangeText={this.props.onTresholdChange}
                    onMorePress={this.props.onTresholdMore}
                    onLessPress={this.props.onTresholdLess} />
                <Text style={styles.category}>Prix normal par défaut</Text>
                <SelectNumber
                    style={styles.price}
                    price={false}
                    priceValue={this.props.default_price}
                    onChangeText={this.props.onDefaultChange}
                    onMorePress={this.props.onDefaultMore}
                    onLessPress={this.props.onDefaultLess} />
                <Text style={styles.category}>Prix spécial par défaut</Text>
                <SelectNumber
                    style={styles.price}
                    price={false}
                    priceValue={this.props.special_price}
                    onChangeText={this.props.onSpecialChange}
                    onMorePress={this.props.onSpecialMore}
                    onLessPress={this.props.onSpecialLess} />
            </View>
        );
    }
}