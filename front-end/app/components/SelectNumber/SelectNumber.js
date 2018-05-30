import React from 'react';
import PropTypes from 'prop-type';
import { TouchableOpacity, View, TextInput, Text } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import styles from './styles';

export default class Header extends React.Component {
    render() {
        return (
            <View style={[styles.container, this.props.style]}>
                <View style={{flex: 1}}>
                    {this.price()}
                    {this.textInput()}
                </View>
                <View style={styles.rightPart}>
                    <TouchableOpacity onPress={this.props.onMorePress} style={styles.button}>
                        <Icon
                            name="plus"
                            size={10}
                            color="#FFF"
                            style={styles.leftIconStyle}
                        />
                    </TouchableOpacity>
                    <TouchableOpacity onPress={this.props.onLessPress} style={styles.button}>
                        <Icon
                            name="minus"
                            size={10}
                            color="#FFF"
                            style={styles.leftIconStyle}
                        />
                    </TouchableOpacity>
                </View>
            </View>
        );
    }

    textInput() {
        if(this.props.disableChangeText == true)
        {
            return (<View style={{flex: 1, padding: 7, backgroundColor: '#eff0f1', borderRadius: 7, marginRight: 10, justifyContent: 'center', alignItems: 'center', borderRadius: 3 }}><Text style={{fontSize: 18}}>{this.props.priceValue}</Text></View>);
        } else {
            return (<TextInput underlineColorAndroid='rgba(0,0,0,0)' keyboardType='numeric' onChangeText={this.props.onChangeText} value={this.props.priceValue} style={styles.textStyle} />)
        }
    }

    price() {
        if(this.props.price)
            return (<Text style={{ flex: 1, textAlign: 'center', marginRight: 10 }}>Prix</Text>);
    }
}