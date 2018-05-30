import EStyleSheet from 'react-native-extended-stylesheet';


export default styles = EStyleSheet.create({
    container: {
        flexDirection: 'row',
        width: '100%',
        height: 'auto',
        padding: 20,
        borderBottomWidth: 1,
        borderBottomColor: '#D9D9D9' 
    },
    leftSide: {
        flex: 1,
    },
    rightSide: {
        width: 'auto',
        height: '100%',
        alignItems: 'flex-end'
    },
    name: {
        color: '#f1a51e',
        fontWeight: 'bold',
        fontSize: 18,
        marginBottom: 2,
    },
    sbeers: {
        color: '#696969',
        fontSize: 18,
        marginBottom: 2,
    },
    benefits: {
        color: '#696969',
        fontSize: 18,
    },
    date: {
        alignSelf: 'flex-end',
        fontSize: 18,
        fontStyle: 'italic',
        color: '#696969',
    },
    editButton: {
        marginTop: 5,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#f1a51e',
        width: 40,
        height: 40,
        borderRadius: 5,
    }
});