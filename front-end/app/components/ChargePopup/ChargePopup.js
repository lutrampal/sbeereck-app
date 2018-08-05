import React from 'react';
import { TouchableOpacity, View, Text, TextInput, Keyboard} from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import DatePicker from 'react-native-datepicker';
import styles from './styles';
import {SelectNumber} from '../SelectNumber';
import QRCodeScanner from 'react-native-qrcode-scanner';

export default class Header extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            qrcode: false
        };
    }

    render() {
        return (
            <View style={{flex: 1}}>
                <Text style={styles.addTitle}>Recharge du compte de {this.props.userName}</Text>
                <Text style={{marginTop: 10}}>{this.props.userName} souhaite mettre <Text style={{color: '#edaf36'}}>{this.props.moneyCount}€</Text> sur son compte.</Text>
                {this.showContent()}
            </View>
        );
    }

    showContent() {
        if(this.state.qrcode) {
            return (
                <View style={{width: '100%', height: 'auto', alignItems: 'center', justifyContent: 'center'}}>
                    <QRCodeScanner
                        onRead={this.onLydiaPayment}
                      />

                      <Text>Si le QR-Code ne fonctionne pas, demandez à la personne d'augmenter la luminosité de son téléphone.</Text>
                </View>
            );
        } else {
            return (
                <View style={{width: '100%', height: 'auto'}}>
                    <TouchableOpacity onPress={() => {
                            if(parseFloat(this.props.moneyCount) < 0.50 || parseFloat(this.props.moneyCount) > 1000) {
                                alert("Lydia n'accepte que des paiements entre 0.50 euros et 1000 euros inclus.")
                            } else {
                                this.setState({qrcode: true})
                            }
                         }} style={{ marginTop: 10, height: 35, width: '100%', borderRadius: 7, alignItems: 'center', justifyContent: 'center', backgroundColor: '#217cc2'}}>
                         <Text style={{color: 'white', fontSize: 18, fontWeight: 'bold'}}>Paiement Lydia</Text>
                    </TouchableOpacity>
                    <TouchableOpacity onPress={() => {this.props.addProduct(); this.props.closePopup();}} style={{ marginTop: 10, height: 35, width: '100%', borderRadius: 7, alignItems: 'center', justifyContent: 'center', backgroundColor: '#edaf36'}}>
                         <Text style={{color: 'white', fontSize: 18, fontWeight: 'bold'}}>Paiement espèces</Text>
                    </TouchableOpacity>
                </View>
            );
        }
    }
}