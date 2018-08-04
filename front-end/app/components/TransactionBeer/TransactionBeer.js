import React from 'react';
import { TouchableOpacity, View, Text, FlatList} from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import { SelectNumber } from '../SelectNumber';
import { RadioGroup, RadioButton } from 'react-native-flexi-radio-button'

export default class Header extends React.Component {
    render() {
        return (
            <View style={{zIndex: 0, width: '100%', flexDirection: 'column', height: 'auto', padding: 10}}>
                <View style={{ flexDirection: 'row' }}>
                    <SelectNumber
                        style={{width: 150, height: 70, marginRight: 10}}
                        priceValue={this.props.beersCount.toString()}
                        onChangeText={(value) => {  }}
                        disableChangeText={true}
                        onMorePress={() => { this.props.setBeersCount(parseInt(this.props.beersCount, 10)+1) }}
                        onLessPress={() => { if (parseInt(this.props.beersCount, 10) > 1) this.props.setBeersCount(parseInt(this.props.beersCount, 10) - 1) }} />

                    <RadioGroup
                        style={{ flexDirection: 'column' }}
                        color='black'
                        selectedIndex={this.selectedIndex()}
                        onSelect={(index, value) => this.props.changeSelectedType(value)}
                    >
                        <RadioButton value={1} color="#fbbc05" style={{ padding: 0, paddingLeft: 5, paddingRight: 5, borderColor: '#fbbc05', marginBottom: 5}} activeColor='#fbbc05'>
                            <Text style={{fontSize: 18}}>Demi</Text>
                        </RadioButton>

                        <RadioButton value={2} color="#fbbc05" style={{ padding: 0, paddingLeft: 5, paddingRight: 5, borderColor: '#fbbc05', marginBottom: 5}} activeColor='#fbbc05'>
                            <Text style={{ fontSize: 18 }}>Pinte</Text>
                        </RadioButton>

                        <RadioButton value={5} color="#fbbc05" style={{ padding: 0, paddingLeft: 5, paddingRight: 5, borderColor: '#fbbc05', marginBottom: 5 }} activeColor='#fbbc05'>
                            <Text style={{ fontSize: 18 }}>Pichet</Text>
                        </RadioButton>
                    </RadioGroup>
                </View>

                <View style={{ flexDirection: 'row', marginTop: 10, marginBottom: 10 }}>
                    <Text style={{ width: '50%', fontSize: 20, height: 'auto', textAlign: 'center', fontStyle: 'italic' }}>
                        {this.calculatePrice().normal}€/{this.getType()}
                    </Text>
                    <Text style={{ width: '50%', fontSize: 20, height: 'auto', color:"#fbbc05", textAlign: 'center', fontStyle: 'italic', fontWeight: 'bold' }}>
                        {this.calculatePrice().special}€/{this.getType()}
                    </Text>
                </View>


                <FlatList
                    data={this.props.beers}
                    extraData={this.props}
                    keyExtractor={(item, index) => index.toString()}
                    renderItem={({ item }) => (
                            <TouchableOpacity style={{flexDirection: 'row', height: 'auto', paddingTop: 10, paddingBottom: 10, borderBottomWidth: 1, borderColor: 'grey'}} onPress={() => this.props.setSelectedBeer(item)}>
                                <Text style={{flex: 1, fontSize: 20, fontWeight: 'bold', color: this.getBeerColor(item.category)}}>
                                    {item.name}
                                </Text>
                                {this.isSelected(item)}
                            </TouchableOpacity>
                        )
                    }
                />

            </View>
        );
    }

    isSelected(beer) {
        if(beer.id === this.props.selectedBeer.id)
        {
            return (
                <Icon
                    name="check"
                    color="#fbbc05"
                    size={20}
                />
            )
        }
    }
    getBeerColor(category) {
        if(category === "special")
            return "#fbbc05";
        else
            return 'black';
    }

    calculatePrice() {
        return { normal: parseFloat(this.props.normal_beer_price) * parseInt(this.props.type), special: parseFloat(this.props.special_beer_price) * parseInt(this.props.type)}
    }

    getType() {
        switch (this.props.type) {
            case 1:
                return "demi";
            case 2:
                return "pinte";
            case 5:
                return "pichet";
        }
    }

    selectedIndex() {
        switch (this.props.type) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 5:
                return 2;
        }
    }
}