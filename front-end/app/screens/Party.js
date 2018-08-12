import React from 'react';
import {FlatList, Keyboard, AsyncStorage, Alert} from 'react-native';
import EStyleSheet from 'react-native-extended-stylesheet';
import {Container} from '../components/Container';
import {Header} from '../components/Header';
import {PartyItem} from '../components/PartyItem';
import {YellowButton} from '../components/YellowButton';
import {SearchBox} from '../components/SearchBox';
import {Popup} from '../components/Popup';
import {Loading} from '../components/Loading'
import {AddParty} from '../components/AddParty';
import {EditParty} from '../components/EditParty';

EStyleSheet.build({
    $mainBackground: '#F9F9F9'
})

export default class Home extends React.Component {
    static navigationOptions = {
        drawerLabel: "Soirées",
    }

    constructor(props) {
        super(props);

        this.state = {
            isSearching: false,
            searchValue: "",
            addPopup: false,
            editPopup: false,
            loading: false,

            appHost: "",
            appToken: "",

            newPartyName: "",
            newPartyDate: "",
            newPartyNormalPrice: "0.00",
            newPartySpecialPrice: "0.00",
            newPartyBeers: [],
            defaultNormalPrice: "0.00",
            defaultSpecialPrice: "0.00",

            editItem: {},
            editPartyName: "",
            editPartyDate: "",
            editPartyNormalPrice: "0.00",
            editPartySpecialPrice: "0.00",
            editPartyBeers: [],

            initialParties: [],
            parties: [],
        };
    }

    async componentDidMount() {
        this.setState({loading: true});

        try {
            let host = await AsyncStorage.getItem('@SbeerEck:host');
            const token = await AsyncStorage.getItem('@SbeerEck:token');

            if (host != null) {
                host = host.replace(" ", "");
                this.setState({appHost: host});
            }
            if (token != null)
                this.setState({appToken: token});

            await this.checkConnection();

            if (this.state.connected) {
                let response = await fetch(
                    'https://' + this.state.appHost + '/default_price/normal_beer',
                    {
                        headers: {
                            'authentication-token': this.state.appToken
                        }
                    });

                let request = await response.json();
                let default_price = request.default_product_price;

                response = await fetch(
                    'https://' + this.state.appHost + '/default_price/special_beer',
                    {
                        headers: {
                            'authentication-token': this.state.appToken
                        }
                    });

                request = await response.json();
                let special_price = request.default_product_price;

                this.setState({
                    defaultNormalPrice: default_price,
                    defaultSpecialPrice: special_price
                });

                this.initialParties();
            }
        } catch (error) {
            alert("Erreur lors du chargement des paramètres.\n" + error);
            this.setState({loading: false});
        }
    }

    render() {
        return (
            <Container>
                <Header
                    title="Soirées"
                    leftButtonIcon="menu"
                    leftButtonAction={() => {
                        this.props.navigation.navigate('DrawerOpen');
                        Keyboard.dismiss()
                    }}
                    rightButtonAction={() => {
                        this.setState(
                            {
                                isSearching: !this.state.isSearching,
                                searchValue: "",
                                parties: this.state.initialParties
                            })
                    }}
                    rightButtonIcon="magnify"/>

                <SearchBox showBox={this.state.isSearching}
                           searchText={this.state.searchValue}
                           editText={(text) => {
                               this.setState(
                                   {
                                       searchValue: text,
                                       parties: this.state.initialParties.filter(
                                           (line) =>
                                               (this.preg(line.name).includes(this.preg(text))))
                                   }
                               );
                           }}/>

                <FlatList
                    refreshing={false}
                    onRefresh={this.initialParties.bind(this)}
                    data={this.state.parties}
                    extraData={this.state}
                    keyExtractor={(item, index) => index.toString()}
                    renderItem={({item}) => <PartyItem item={item}
                                                       onEditClick={(theItem) => {
                                                           this.getPartyDetails(theItem)
                                                       }} onItemClick={(theItem) => {
                        this.props.navigation.navigate('Transactions', {
                            partyId: theItem.id,
                            partyName: theItem.name,
                            refreshItems: (() => {
                                this.initialParties()
                            }).bind(this)
                        })
                    }}/>}
                />
                <YellowButton buttonIcon="plus" buttonAction={() => {
                    this.setState({
                        addPopup: true,
                        newPartyBeers: [],
                        newPartyDate: "",
                        newPartyName: "",
                        newPartyNormalPrice: this.state.defaultNormalPrice,
                        newPartySpecialPrice: this.state.defaultSpecialPrice
                    })
                }}/>

                <Popup shown={this.state.addPopup}
                       toggleState={() => this.setState({addPopup: !this.state.addPopup})}>
                    <AddParty
                        onChangePartyName={(text) => {
                            this.setState({newPartyName: text})
                        }}
                        newPartyName={this.state.newPartyName}
                        onDateChange={(text) => {
                            this.setState({newPartyDate: text})
                        }}
                        newPartyDate={this.state.newPartyDate}

                        onNormalMorePress={() => {
                            this.setState({
                                newPartyNormalPrice: (parseFloat(this.state.newPartyNormalPrice) + 0.01).toFixed(2).toString()})
                        }}
                        onNormalLessPress={() => {
                            this.setState({newPartyNormalPrice: (parseFloat(this.state.newPartyNormalPrice) - 0.01).toFixed(2).toString()})
                        }}
                        onChangeNormalPrice={(text) => {
                            let editedText = this.setPrice(text);
                            this.setState({newPartyNormalPrice: editedText})
                        }}

                        normalPriceValue={this.state.newPartyNormalPrice}

                        onSpecialMorePress={() => {
                            this.setState({newPartySpecialPrice: (parseFloat(this.state.newPartySpecialPrice) + 0.01).toFixed(2).toString()})
                        }}
                        onSpecialLessPress={() => {
                            this.setState({newPartySpecialPrice: (parseFloat(this.state.newPartySpecialPrice) - 0.01).toFixed(2).toString()})
                        }}
                        onChangeSpecialPrice={(text) => {
                            let editedText = this.setPrice(text);
                            this.setState({newPartySpecialPrice: editedText})
                        }}
                        specialPriceValue={this.state.newPartySpecialPrice}

                        onAddBeersPress={() => {
                            this.props.navigation.navigate('AddBeer', {
                                onValidatePress: this.onAddBeerValidatePress.bind(this),
                                beers: this.state.newPartyBeers
                            })
                        }}

                        onValidatePress={() => {
                            this.setState({loading: true});
                            this.createParty();
                            this.setState({addPopup: false})
                        }}
                    />
                </Popup>

                <Popup shown={this.state.editPopup}
                       toggleState={() => this.setState({editPopup: !this.state.editPopup})}>
                    <EditParty
                        item={this.state.editItem}
                        onChangePartyName={(text) => {
                            this.setState({editPartyName: text})
                        }}
                        editPartyName={this.state.editPartyName}

                        onDateChange={(text) => {
                            this.setState({editPartyDate: text})
                        }}
                        editPartyDate={this.state.editPartyDate}

                        onNormalMorePress={() => {
                            this.setState({editPartyNormalPrice: (parseFloat(this.state.editPartyNormalPrice) + 0.01).toFixed(2).toString()})
                        }}
                        onNormalLessPress={() => {
                            this.setState({editPartyNormalPrice: (parseFloat(this.state.editPartyNormalPrice) - 0.01).toFixed(2).toString()})
                        }}
                        onChangeNormalPrice={(text) => {
                            let editedText = this.setPrice(text);
                            this.setState({editPartyNormalPrice: editedText})
                        }}
                        normalPriceValue={this.state.editPartyNormalPrice}

                        onSpecialMorePress={() => {
                            this.setState({editPartySpecialPrice: (parseFloat(this.state.editPartySpecialPrice) + 0.01).toFixed(2).toString()})
                        }}
                        onSpecialLessPress={() => {
                            this.setState({editPartySpecialPrice: (parseFloat(this.state.editPartySpecialPrice) - 0.01).toFixed(2).toString()})
                        }}
                        onChangeSpecialPrice={(text) => {
                            let editedText = this.setPrice(text);
                            this.setState({editPartySpecialPrice: editedText})
                        }}
                        specialPriceValue={this.state.editPartySpecialPrice}

                        onEditBeersPress={() => {
                            this.props.navigation.navigate('AddBeer', {
                                onValidatePress: this.onEditBeerValidatePress.bind(this),
                                beers: this.state.editPartyBeers
                            })
                        }}

                        onSuppressPress={(item) => {
                            Alert.alert(
                                'Supprimer une soirée ',
                                'Êtes-vous sûr de vouloir supprimer le soirée ' + item.name + ' ?',
                                [
                                    {
                                        text: 'Annuler', onPress: () => {
                                            this.setState({editPopup: false})
                                        }, style: 'cancel'
                                    },
                                    {
                                        text: 'Supprimer', onPress: () => {
                                            this.setState({loading: true});
                                            this.deleteParty(item);
                                            this.setState({editPopup: false})
                                        }
                                    },
                                ],
                                {cancelable: false}
                            )
                        }}
                        onValidatePress={(item) => {
                            this.setState({loading: true});
                            this.updateParty(item);
                            this.setState({editPopup: false})
                        }}
                    />
                </Popup>
                <Loading shown={this.state.loading}/>
            </Container>
        );
    }

    async initialParties() {
        await this.checkConnection();

        this.setState({loading: true});

        try {
            let response = await fetch('https://' + this.state.appHost + '/parties',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            let request = await response.json();

            this.setState({initialParties: request});

            this.setState({
                parties: this.state.parties.concat(this.state.initialParties),
                loading: false,
                searchValue: ""
            });
        } catch (error) {
            this.setState({loading: false, searchValue: ""});
            console.log(error);
        }
    }

    async createParty() {
        await this.checkConnection();

        try {
            if (this.state.connected) {
                let response = await fetch('https://' + this.state.appHost + '/parties',
                    {
                        method: "post",
                        headers: {
                            'content-type': "application/json",
                            'authentication-token': this.state.appToken
                        },
                        body: JSON.stringify({
                            "name": this.state.newPartyName,
                            "date": this.state.newPartyDate,
                            "normal_beer_price": this.state.newPartyNormalPrice,
                            "special_beer_price": this.state.newPartySpecialPrice,
                            "served_beers": this.state.newPartyBeers
                        })
                    });

                this.initialParties();
                alert("Soirée ajoutée !");
            }
        } catch (error) {
            console.log(error);
            this.setState({loading: false});
        }
    }

    async updateParty(item) {
        await this.checkConnection();

        try {
            if (this.state.connected) {
                let response = await fetch('https://' + this.state.appHost + '/parties/' + item.id,
                    {
                        method: "put",
                        headers: {
                            'content-type': "application/json",
                            'authentication-token': this.state.appToken
                        },
                        body: JSON.stringify({
                            "name": this.state.editPartyName,
                            "date": this.state.editPartyDate,
                            "normal_beer_price": this.state.editPartyNormalPrice,
                            "special_beer_price": this.state.editPartySpecialPrice,
                            "served_beers": this.state.editPartyBeers
                        })
                    });

                this.initialParties();
                alert("Soirée modifiée !");
            }
        } catch (error) {
            console.log(error);
            this.setState({loading: false});
        }
    }

    async deleteParty(item) {
        await this.checkConnection();

        try {
            if (this.state.connected) {
                let response = await fetch('https://' + this.state.appHost + '/parties/' + item.id,
                    {
                        method: "delete",
                        headers: {
                            'content-type': "application/json",
                            'authentication-token': this.state.appToken
                        }
                    });

                this.initialParties();
                alert("Soirée supprimée !");
            }
        } catch (error) {
            console.log(error);
            this.setState({loading: false});
        }
    }

    async checkConnection() {
        try {
            let response = await fetch('https://' + this.state.appHost + '/default_price/normal_beer',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            this.setState({connected: true});

        } catch (error) {
            this.setState({connected: false, loading: false});
            this.props.navigation.navigate("Parameters");
        }
    }

    async getPartyDetails(item) {
        this.setState({loading: true})
        try {
            let response = await fetch('https://' + this.state.appHost + '/parties/' + item.id,
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            let theItem = await response.json();

            console.log(theItem)

            this.setState({
                editPopup: true,
                editItem: theItem,
                editPartyBeers: theItem.served_beers,
                editPartyDate: theItem.date,
                editPartyName: theItem.name,
                editPartyNormalPrice: theItem.normal_beer_price,
                editPartySpecialPrice: theItem.special_beer_price
            })

            this.setState({loading: false});

        } catch (error) {
            this.setState({loading: false});
            console.log(error);
        }
    }

    setPrice(text) {
        if (text === "" || text === "0")
            return "0";
        else
            return text.toString().replace(",", ".");
    }

    onEditBeerValidatePress(beers) {
        let resultedBeers = [];
        beers.forEach(myPartyBeer => {
            if (myPartyBeer.category != null && myPartyBeer.category !== undefined) {
                resultedBeers.push(
                    {
                        name: myPartyBeer.name,
                        id: myPartyBeer.id,
                        price: myPartyBeer.price,
                        category: myPartyBeer.category
                    }
                )
            }
        });

        this.setState({editPartyBeers: resultedBeers});
    }

    onAddBeerValidatePress(beers) {
        let resultedBeers = [];
        beers.forEach(myPartyBeer => {
            if (myPartyBeer.category != null && myPartyBeer.category !== undefined) {
                resultedBeers.push(
                    {
                        name: myPartyBeer.name,
                        id: myPartyBeer.id,
                        price: myPartyBeer.price,
                        category: myPartyBeer.category
                    }
                )
            }
        });

        this.setState({newPartyBeers: resultedBeers});
    }

    preg(chaine) {
        let tab = {
            "é": "e", "è": "e", "ê": "e", "ë": "e",
            "ç": "c",
            "à": "a", "â": "a", "ä": "a",
            "î": "i", "ï": "i",
            "ù": "u",
            "ô": "o", "ó": "o", "ö": "o",
            " ": "", "'": "", "-": ""
        };

        for (i in tab) {
            chaine = chaine.toLowerCase().replace(new RegExp(i, "gi"), tab[i])
        }
        return chaine;
    }
}
