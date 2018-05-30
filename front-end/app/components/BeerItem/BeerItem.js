import React from 'react';
import PropTypes from 'prop-type';
import { TouchableOpacity, View, Text } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import styles from './styles';
import {RadioGroup, RadioButton} from 'react-native-flexi-radio-button'

export default class Header extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        radio_props = [
            { label: 'Non', value: 0 },
            { label: 'Normale', value: 'normal' },
            { label: 'Spéciale', value: 'special' }
        ];

        return (
            <View style={styles.container}>
                <View style={styles.leftSide}>
                    <Text style={styles.name}>{this.props.item.name}</Text>
                </View>
                <View style={styles.rightSide}>
                    <RadioGroup
                        style={{flexDirection: 'row'}}
                        color='black'
                        selectedIndex={this.selectedIndex()}
                        onSelect={(index, value) => this.props.onPress(value)}
                    >
                        <RadioButton value={null} style={{ padding: 0, paddingLeft: 5, paddingRight: 5, flexDirection: 'column', alignItems: "center", justifyContent: 'center' }} >
                            <Text>Non</Text>
                        </RadioButton>

                        <RadioButton value={'normal'} style={{ padding: 0, paddingLeft: 5, paddingRight: 5, flexDirection: 'column', alignItems: "center", justifyContent: 'center' }}>
                            <Text>Normale</Text>
                        </RadioButton>

                        <RadioButton value={'special'} color="#fbbc05" style={{ padding: 0, paddingLeft: 5, paddingRight: 5, borderColor: '#fbbc05', flexDirection: 'column', alignItems: "center", justifyContent: 'center'}} activeColor='#fbbc05'>
                            <Text style={{ color: '#fbbc05'}}>Spéciale</Text>
                        </RadioButton>
                    </RadioGroup>
                </View>
            </View>
        );
    }

    selectedIndex() {
        switch(this.props.item.category) {
            case 'special':
                return 2;
            case 'normal': 
                return 1;
            default:
                return 0;
        }
    }
}