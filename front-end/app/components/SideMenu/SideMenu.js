import React, { Component } from 'react';
import { ScrollView, Text, View, Image, TouchableOpacity } from 'react-native';
import styles from './styles';
import PropTypes from 'prop-types';
import Icon from 'react-native-vector-icons/FontAwesome';

class DrawerContent extends Component {
  render() {
    return (
      <View style={styles.container}>
        <ScrollView alwaysBounceVertical={false}>
          <View style={{width: '100%', height: 150, alignItems: 'center'}}>
            <Image
              style={{width: 200, height: 150}}
              source={require('../../../sbeereck.png')}
            />
          </View>
          <TouchableOpacity onPress={() => this.props.navigation.navigate('Party')}>
            <Text style={styles.button}>
              Soirées
            </Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={() => this.props.navigation.navigate('Members')}>
            <Text style={styles.button}>
              Adhérents
            </Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={() => this.props.navigation.navigate('Products')}>
            <Text style={[styles.button, {marginBottom: 20}]}>
              Bières et autres
            </Text>
          </TouchableOpacity>
          <TouchableOpacity style={{borderTopWidth: 1, borderColor: '#31363A'}} onPress={() => this.props.navigation.navigate('Parameters')}>
            <Text style={styles.button}>
              <Icon name="cog" size={20} /> Paramètres
            </Text>
          </TouchableOpacity>
          
        </ScrollView>
      </View>
    );
  }
}
DrawerContent.propTypes = {
  navigation: PropTypes.object
};
export default DrawerContent;