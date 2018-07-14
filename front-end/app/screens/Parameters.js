import React from 'react';
import { Keyboard, AsyncStorage } from 'react-native';
import EStyleSheet from 'react-native-extended-stylesheet';
import { Container } from '../components/Container';
import {Header} from '../components/Header';
import { Loading } from '../components/Loading';
import { Authentification } from '../components/Authentification';
import { AppParam } from '../components/AppParam';
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'

EStyleSheet.build({
    $mainBackground: '#F9F9F9'
})

export default class Home extends React.Component {
    constructor(props){
        super(props);

        this.state = {
            loading: false,
            connected: false,
            defaultChanged: false,
            specialChanged: false,
            tresholdChanged: false,
            appHost: "",
            appToken: "",
            default_price: "0.00",
            special_price: "0.00",
            balance_treshold: "0.00",
        }

    }

    componentDidMount() {
        this.loadData();
    }

    static navigationOptions = {
        drawerLabel: "Paramètres",
    }

    render() {
        return(
            <Container>
                <Header
                    title="Paramètres"
                    leftButtonIcon="menu"
                    leftButtonAction={() => { this.props.navigation.navigate('DrawerOpen'); Keyboard.dismiss()}}
                    rightButtonAction={this.saveParameters.bind(this)}
                    rightButtonIcon="check"
                />
                <KeyboardAwareScrollView alwaysBounceVertical={false}>
                    <Authentification
                        appHost={this.state.appHost}
                        appToken={this.state.appToken}

                        editAppHost={(appHost) => { this.setState({ appHost: appHost }) }}
                        editAppToken={(appToken) => { this.setState({ appToken: appToken }) }}
                    />
                    {this.appParams()}

                </KeyboardAwareScrollView>

                <Loading shown={this.state.loading} />
            </Container>
        );
    }

    appParams() {
        if(this.state.connected === true)
            return (<AppParam
                default_price={this.state.default_price}
                special_price={this.state.special_price}
                balance_treshold={this.state.balance_treshold}

                onDefaultChange={(text) => {let texts = this.setPrice(text); this.setState({ defaultChanged: true, default_price: texts })}}
                onDefaultMore={() => { this.setState({ defaultChanged: true, default_price: (parseFloat(this.state.default_price) + 0.01).toFixed(2).toString() }) }}
                onDefaultLess={() => { this.setState({ defaultChanged: true, default_price: (parseFloat(this.state.default_price) - 0.01).toFixed(2).toString() }) }}

                onSpecialChange={(text) => {let texts = this.setPrice(text); this.setState({ specialChanged: true, special_price: texts })}}
                onSpecialMore={() => { this.setState({ specialChanged: true, special_price: (parseFloat(this.state.special_price) + 0.01).toFixed(2).toString() }) }}
                onSpecialLess={() => { this.setState({ specialChanged: true, special_price: (parseFloat(this.state.special_price) - 0.01).toFixed(2).toString() }) }}

                onTresholdChange={(text) => {let texts = this.setPrice(text); this.setState({ tresholdChanged: true, balance_treshold: texts })}}
                onTresholdMore={() => { this.setState({ tresholdChanged: true, balance_treshold: (parseFloat(this.state.balance_treshold) + 0.01).toFixed(2).toString() }) }}
                onTresholdLess={() => { this.setState({ tresholdChanged: true, balance_treshold: (parseFloat(this.state.balance_treshold) - 0.01).toFixed(2).toString() }) }}
            />);
    }

    setPrice(text) {
        if (text === "" || text === "0")
            return "0";
        else
            return text.toString().replace(",", ".");
    }

    async saveParameters() {
        this.setState({ loading: true });

        try {
            await AsyncStorage.setItem('@SbeerEck:host', this.state.appHost);
            await AsyncStorage.setItem('@SbeerEck:token', this.state.appToken);

            await this.checkConnection();

            if(this.state.connected) {
                if(this.state.defaultChanged)
                {
                    let response = await fetch('https://' + this.state.appHost + '/default_price/normal_beer',
                        {
                            method: "put",
                            headers: {
                                'content-type': "application/json",
                                'authentication-token': this.state.appToken
                            },
                            body: JSON.stringify({
                                default_product_price: this.state.default_price
                            })
                        });

                    this.setState({defaultChanged: false});
                }
                if(this.state.specialChanged)
                {
                    let response = await fetch('https://' + this.state.appHost + '/default_price/special_beer',
                        {
                            method: "put",
                            headers: {
                                'content-type': "application/json",
                                'authentication-token': this.state.appToken
                            },
                            body: JSON.stringify({
                                default_product_price: this.state.special_price
                            })
                        });
                    this.setState({specialChanged: false});
                }
                if(this.state.tresholdChanged)
                {
                    let response = await fetch('https://' + this.state.appHost + '/balance_too_low_threshold',
                        {
                            method: "put",
                            headers: {
                                'content-type': "application/json",
                                'authentication-token': this.state.appToken
                            },
                            body: JSON.stringify({
                                balance_too_low_threshold: this.state.balance_treshold
                            })
                        });
                    this.setState({ tresholdChanged: false});
                }
                this.loadParameters();
            }
        } catch (error) {
            alert("Erreur lors de la sauvegarde des paramètres.\n" + error);
            this.setState({ loading: false });
        }
    }

    async loadData() {
        this.setState({ loading: true });

        try {
            let host = await AsyncStorage.getItem('@SbeerEck:host');
            const token = await AsyncStorage.getItem('@SbeerEck:token');

            if (host != null) {
                host = host.replace(" ", "");
                this.setState({ appHost: host });
            }
            if(token != null)
                this.setState({ appToken: token });

            await this.checkConnection();

            if(this.state.connected)
                this.loadParameters();
        } catch (error) {
            alert("Erreur lors du chargement des paramètres.\n" + error);
            this.setState({ loading: false });
        }
    }

    async loadParameters() {
        try {
            let response = await fetch('https://' + this.state.appHost + '/default_price/normal_beer',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            let request = await response.json();
            let default_price = request.default_product_price;

            response = await fetch('https://' + this.state.appHost + '/default_price/special_beer',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            request = await response.json();
            let special_price = request.default_product_price;

            response = await fetch('https://' + this.state.appHost + '/balance_too_low_threshold',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            request = await response.json();
            let balance_treshold = request.balance_too_low_threshold;

            this.setState({ default_price: default_price.toString(), special_price: special_price.toString(), balance_treshold: balance_treshold.toString(), connected: true});

        } catch (error) {
            this.setState({ loading: false });
            console.log(error);
        }

        this.setState({ loading: false });
    }

    async checkConnection()  {
        try {
            let response = await fetch('https://' + this.state.appHost + '/default_price/normal_beer',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            this.setState({ connected: true });

        } catch (error) {
            this.setState({ connected: false, loading: false });
            alert("Veuillez vous connecter en entrant votre pseudonyme (username) et votre mot de passe (password).");
        }
    }
}
